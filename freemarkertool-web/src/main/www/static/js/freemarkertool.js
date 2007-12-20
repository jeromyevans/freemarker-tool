/*global FreemarkerTool,blueskyminds,TrimPath,YAHOO*/
blueskyminds.namespace("FreemarkerTool");

FreemarkerTool.constants = {
    FORM_ID : "templateForm",
    OPEN_CONTAINER_ID : "openContainer",
    OPEN_CONTAINER_BOX_ID : "openContainerBox",
    OPEN_TEXT_ID : "open",
    CLOSE_CONTAINER_ID : "closeContainer",
    CLOSE_CONTAINER_BOX_ID : "closeContainerBox",
    CLOSE_TEXT_ID : "close",
    BODY_TEXT_CONTAINER_ID : "bodyTextContainer",
    BODY_TEXT_CONTAINER_BOX_ID : "bodyTextContainerBox",
    BODY_TEXT_ID : "bodyText",
    OUTPUT_TEXT_CONTAINER_ID : "outputTextContainer",
    OUTPUT_TEXT_ID : "outputText",
    ERROR_ID : "errorContainer",
    INDICATOR_ID : "indicator",
    TOGGLE_VIEW_BTN_ID : "toggleViewBtnGroup",
    OPEN_TITLE_TEMPLATE_ID : "openTitleTemplate",
    OPEN_TITLE_TAG_ID : "openTitleTag"
};

FreemarkerTool.ui = function() {

    /** The number of milliseconds to wait befteore posting a request */
    var CHANGE_TIMEOUT = 1000;
    var TICK_INTERVAL = 250;

    var ERROR_EVENT = "error";

    var errorController;
    var idleTimer;
    var indicatorController;

    var openTextController;
    var closingTextController;
    var bodyTextController;

    /** Indicator to control whether a refresh is required */
    var inputChanged = false;

    var contextFields;

    /** Calls the callback if not reset for the specifid number of ticks to wait */
    var IdleTimer = function(tickInterval, millisToWait, callback) {

        var tickCounter;
        var lastFired;
        var ticksToWait;

        /** Executed every interval tick */
        function tickListener() {
            tickCounter++;
            if (tickCounter-lastFired > ticksToWait) {
                callback();
                lastFired = tickCounter;
            }
        }

        function init() {
            tickCounter = 0;
            lastFired = 0;
            ticksToWait = (millisToWait / tickInterval);
            setInterval(tickListener, tickInterval);
        }        

        init();

        return {
            reset : function() {
                tickCounter = 0;
                lastFired = 0;
            }
        };
    };

    /** A controller for the text inputs
    * @param id  id of the text element
    * @param containerId id of the container element
    * @param idleTimer an IdleTimer used to detect when the input has been idle since the last update
    * @param changedCallback callback to notify */
    var TextInputController = function(id, containerId, callback) {

        var container = containerId;
        var elementId = id;
        var element;
                
        function onChangeListener(e) {
            callback();
        }

        function onFocusListener(e) {
            YAHOO.util.Dom.setStyle(container, "background-color", "#ffffe8");
        }

        function onBlurListener(e) {
            YAHOO.util.Dom.setStyle(container, "background-color", "white");
        }

        element = new YAHOO.util.Element(id);
        element.on('keyup', onChangeListener);
        element.on('keydown', onChangeListener);
        element.on('keypress', onChangeListener);
        element.on('focus', onFocusListener);
        element.on('blur', onBlurListener);

        return {
            setContent: function(text) {
                var el = document.getElementById(elementId);
                if (el) {
                    if (text) {
                        el.value = text;
                    } else {
                        el.value = "";
                    }
                }
            }
        };
    };   

    /** Updates the value displayed in the ouput */
    function setFreemarkerOutput(text) {
        var el = document.getElementById(FreemarkerTool.constants.OUTPUT_TEXT_ID);
        if (el) {
            el.value = text;
        }
    }

    function clearInputChangedFlag() {
        inputChanged = false;
    }

    function setInputChangedFlag() {
        inputChanged = true;
    }

    function isInputChanged() {
        return inputChanged;
    }

    function inputChangedListener() {
        idleTimer.reset();
        setInputChangedFlag();
    }

    function showIndicator() {
        blueskyminds.events.fire("startedParsing");
    }

    function hideIndicator() {
        blueskyminds.events.fire("stoppedParsing");
    }

    /** private callback for the asyncRequest */
    var parseCallback = {
        success: function(o) {
            hideIndicator();
            var payload = blueskyminds.net.json.evaluate(o.responseText);
            if (payload) {
                if (payload.exception) {
                    blueskyminds.events.fire(ERROR_EVENT, payload.exception.message);
                } else {
                    setFreemarkerOutput(payload.result);
                }
            } else {
                blueskyminds.events.fire(ERROR_EVENT, "Invalid response received from the server");
            }
        },
        failure: function(o) {
            hideIndicator();
            blueskyminds.events.fire(ERROR_EVENT, blueskyminds.net.errorMessage(o));
        }
    };

    /** Invokes an async request to parse the inputs */
    function parseTemplate() {
        var formEl = document.getElementById(FreemarkerTool.constants.FORM_ID);

        FreemarkerTool.ui.dismissErrors();

        if (formEl) {
           YAHOO.util.Connect.setForm(formEl);
           showIndicator();
           YAHOO.util.Connect.asyncRequest('POST', formEl.action, parseCallback);
        } else {
            blueskyminds.events.fire(ERROR_EVENT, "Form '"+FreemarkerTool.constants.FORM_ID+"' is missing");
        }
    }

    /** When idle, check if the input has changed, and if so parse the template */
    function idleCallback() {
        if (isInputChanged()) {
            clearInputChangedFlag();
            parseTemplate();
        }
    }

    var CONTEXT_CONTAINER_ID = "context[{0}].container";
    var PARENT_CONTEXT_CONTAINER_ID = "contextContainer";
    var CONTEXT_TEMPLATE_ID = "contextFieldTemplate";
    var CONTEXT_ENABLED_CHECKBOX_ID = "context[{0}].enabled";
    var CONTEXT_NAME_TEXT_ID = "context[{0}].name";
    var CONTEXT_VALUE_TEXT_ID = "context[{0}].value";
    var CONTEXT_VALUE_NULL_CHECKBOX_ID = "context[{0}].nullValue";
    var contextFieldTemplate;
    var contextFieldCount;

    /** A ContextField holds the model for the fields used to define each item in the context
    * @param fieldIndex sequential number of this field
    * @param listener to fire when focus is obtained
    **/
    var ContextField = function(fieldIndex, onFocusListener, onChangeListener) {

        /** Index of this ContextField */
        var index = fieldIndex;
        var containerEl;
        var enabledEl;
        var nameEl;
        var valueEl;
        var nullEl;

        var nameElId;
        var valueElId;

        var lastValue = "";

        var focusListener = onFocusListener;
        var changeListener = onChangeListener;

        /** Notifies the listner that this ContextField has focus */
        function fireFocusListener() {
            if (focusListener) {
                focusListener(index);
            }
        }

        /** Notifies the listener that this ContextField has changed state */
        function fireChangeListener() {
            if (changeListener) {
                changeListener(index);
            }
        }

        function setEnabledFlag(enabled) {
            if (enabled) {
                enabledEl.set('checked', true);
                enabledEl.set('value', "true");
            } else {
                enabledEl.set('checked', false);
                enabledEl.set('value', "false");
            }
        }

        function isEnabled() {
            return enabledEl.get('checked');
        }

        function isNull() {
            return nullEl.get('checked');
        }

        function setNullFlag(nullFlag) {
            if (nullFlag) {
                nullEl.set('checked', true);
                nullEl.set('value', "true");
            } else {
                nullEl.set('checked', false);
                nullEl.set('value', "false");
            }
        }

        function setNullValue(checked) {
            setNullFlag(checked);
            if (checked) {
                valueEl.addClass('disabled');
                // save previous value
                if (lastValue === null) {
                    var value = valueEl.get('element').value;
                    lastValue = (value ? value : "");
                    valueEl.set('value', "< null >");
                }
            } else {
                valueEl.removeClass('disabled');
                if (lastValue !== null) {
                   // restore last value
                   valueEl.set('value', lastValue);
                   lastValue = null;
                }
            }
        }

        function enable() {
            setEnabledFlag(true);
            containerEl.removeClass("disabled");
            containerEl.addClass("enabled");
            nameEl.removeClass('disabled');
            nullEl.removeClass('disabled');
            setNullValue(isNull());
        }

        function disable() {
            setEnabledFlag(false);
            containerEl.removeClass("enabled");
            containerEl.addClass("disabled");
            nameEl.addClass('disabled');
            valueEl.addClass('disabled');
            nullEl.addClass('disabled');
        }

        /** Merges the index into a template used to determine the ID */
        function idOf(template) {
            return YAHOO.Tools.printf(template, index);
        }

        function onEnabledChange(e) {
            if (e.target.checked) {
                enable();
                fireFocusListener();
            } else {
                disable();
            }
            fireChangeListener();
        }

        function clearName() {
            nameEl.set('value', "");
        }

        function clearValue() {
            valueEl.set('value', "");
        }

        function onNameChange(e) {
            enable();
            fireChangeListener();
        }

        function onNameFocus(e) {
            enable();
            YAHOO.util.Dom.addClass(nameElId, "focus");
            fireFocusListener();
        }

        function onNameBlur(e) {
            YAHOO.util.Dom.removeClass(nameElId, "focus");
        }

        function onValueChange(e) {
            enable();
            fireChangeListener();
        }

        function onValueFocus(e) {
            enable();
            YAHOO.util.Dom.addClass(nameElId, "focus");
            fireFocusListener();
        }

        function onValueBlur(e) {
            YAHOO.util.Dom.removeClass(nameElId, "focus");
        }

        /** Listener for a change in the null checkox.  
         * Enables the row, fires the listener and resets the idle timer
         * @param e
         */
        function onNullChange(e) {
            setNullValue(e.target.checked);
            enable();
            fireFocusListener();
            fireChangeListener();
        }

        function reset() {
            if (isEnabled()) {
                enable();
            } else {
                disable();
            }
        }

        function init() {
            containerEl = new YAHOO.util.Element(idOf(CONTEXT_CONTAINER_ID));
            enabledEl = new YAHOO.util.Element(idOf(CONTEXT_ENABLED_CHECKBOX_ID));
            nameElId = idOf(CONTEXT_NAME_TEXT_ID);
            nameEl = new YAHOO.util.Element(nameElId);
            valueElId = idOf(CONTEXT_VALUE_TEXT_ID);
            valueEl = new YAHOO.util.Element(valueElId);
            nullEl = new YAHOO.util.Element(idOf(CONTEXT_VALUE_NULL_CHECKBOX_ID));

            if (enabledEl) {
                enabledEl.on('click', onEnabledChange);
            }

            if (nameEl) {
                nameEl.on('keyup', onNameChange);
                nameEl.on('keydown', onNameChange);
                nameEl.on('keypress', onNameChange);
                nameEl.on('focus', onNameFocus);
                nameEl.on('blur', onNameBlur);
                nameEl.on('click', onNameFocus);
            }

            if (valueEl) {
                valueEl.on('keyup', onValueChange);
                valueEl.on('keydown', onValueChange);
                valueEl.on('keypress', onValueChange);
                valueEl.on('focus', onValueFocus);
                valueEl.on('blur', onValueBlur);
                valueEl.on('click', onValueFocus);
            }

            if (nullEl) {
                nullEl.on('click', onNullChange);
            }

            reset();
        }

        init();

        return {
            isEnabled : function() {
                if (enabledEl) {
                    return enabledEl.checked;
                } else {
                    return false;
                }
            },
            clear : function() {
                disable();
                clearName();
                clearValue();
                setNullFlag(false);
            },
            setName : function(name) {
                nameEl.set('value', name);
            },
            setValue : function(value) {
                valueEl.set('value', value);
            },
            setNullValue : function(isNull) {
                setNullFlag(isNull);
                lastValue = null;  // we don't need to remember the last value
            },
            setEnabled : function(enabled) {
                if (enabled) {
                    enable();
                } else {
                    disable();
                }
            }
        };
    };

        /**
     * Listener for focus in a context field.  Used to create a new context field
     *  instance if the focus is in the last one
     * @param itemNo
     */
    function onContextFieldFocus(itemNo) {
        if (itemNo == contextFields.length - 1) {
            createContextField(itemNo+1);
        }
    }

    /**
     * Listener for for change of state in a context field.  Triggers a new repost
     * @param itemNo
     */
    function onContextFieldChange(itemNo) {
        idleTimer.reset();
        setInputChangedFlag();
    }

    /** Creates a td and input element with the id prepared from the
         id value and template */
    function prepareInput(id, idTemplate, type, className) {

        var eTD = YAHOO.util.Dom.create("td");
        var idValue = YAHOO.tools.printf(idTemplate, id);
        var eInput = YAHOO.util.Dom.create("input", {
            id : idValue,
            type: type,
            className: className,
            name: idValue
        });

        eTD.appendChild(eInput);

        return eTD;
     }


    /**
     * Adds a new ContextField to the DOM and initialise a controller for it
     * If the ContxtField already exists its cleared
     * @param itemNo
     * @return contextField instance
     */
    function createContextField() {

        var templateContext = {
            index : contextFieldCount
        };

        var nodeId = YAHOO.tools.printf(CONTEXT_CONTAINER_ID, templateContext.index);
        var node = document.getElementById(nodeId);

        if (!node) {
            node = YAHOO.util.Dom.create("tr", {
                id : nodeId,
                className : "contextField"
            });

            node.appendChild(prepareInput(templateContext.index, CONTEXT_ENABLED_CHECKBOX_ID, "checkbox", ""));
            node.appendChild(prepareInput(templateContext.index, CONTEXT_NAME_TEXT_ID, "text", "name"));
            node.appendChild(prepareInput(templateContext.index, CONTEXT_VALUE_TEXT_ID, "text", "value"));
            node.appendChild(prepareInput(templateContext.index, CONTEXT_VALUE_NULL_CHECKBOX_ID, "checkbox", ""));

            var parent = document.getElementById(PARENT_CONTEXT_CONTAINER_ID);
            parent.appendChild(node);

            // insert the HTML into the DOM
            //node.innerHTML = contextFieldTemplate.process(templateContext);

            // initialise the controller
            contextFields[templateContext.index] = new ContextField(templateContext.index, onContextFieldFocus, onContextFieldChange);
        } else {
            // this field already exists
            contextFields[templateContext.index].clear();
        }
        contextFieldCount++;

        return contextFields[templateContext.index];
    }

    /**
     * Setup the UI controls to define the context
     */
    function initContext() {
        contextFields = [];
        contextFieldCount = 0;

        contextFieldTemplate = TrimPath.parseDOMTemplate(CONTEXT_TEMPLATE_ID);
        
        // remove the hard-coded context fields
        var elements = YAHOO.util.Dom.getElementsByClassName("contextField");
        var parentNode;
        for (var itemNo = 0; itemNo < elements.length; itemNo++) {
            parentNode = elements[itemNo].parentNode;
            parentNode.removeChild(elements[itemNo]);
        }

        // setup the first field
        createContextField(0);
    }

    var ERROR_TEMPLATE_ID = "errorTemplate";

    function init() {
        FreemarkerTool.layout.init();

        var errorTemplate = TrimPath.parseDOMTemplate(ERROR_TEMPLATE_ID);

        // the error controller reports errors
        errorController = new blueskyminds.ui.ErrorController(FreemarkerTool.constants.ERROR_ID, ERROR_EVENT, errorTemplate);

        // the idle timer waits for idle input before posting changes
        idleTimer = new IdleTimer(TICK_INTERVAL, CHANGE_TIMEOUT, idleCallback);

        // controllers for each of the widgets
        openTextController = new TextInputController(FreemarkerTool.constants.OPEN_TEXT_ID, FreemarkerTool.constants.OPEN_CONTAINER_BOX_ID, inputChangedListener);
        bodyTextController = new TextInputController(FreemarkerTool.constants.BODY_TEXT_ID, FreemarkerTool.constants.BODY_TEXT_CONTAINER_BOX_ID, inputChangedListener);
        closingTextController = new TextInputController(FreemarkerTool.constants.CLOSE_TEXT_ID, FreemarkerTool.constants.CLOSE_CONTAINER_BOX_ID, inputChangedListener);

        // setup the connection manager listeners
        blueskyminds.events.register("startedParsing", new YAHOO.util.CustomEvent("startedParsing"));
        blueskyminds.events.register("stoppedParsing", new YAHOO.util.CustomEvent("stoppedParsing"));

        indicatorController = new blueskyminds.ui.ProgressIndicatorController(FreemarkerTool.constants.INDICATOR_ID, "startedParsing", "stoppedParsing");

        initContext();

        var openText = document.getElementById(FreemarkerTool.constants.OPEN_TEXT_ID);
        openText.select();
        openText.focus();
    }

    YAHOO.util.Event.onDOMReady(init);

    return {
        dismissErrors : function() {
            errorController.dismissErrors();
        },
        setViewMode: function(template) {
            FreemarkerTool.layout.setTemplateView(template);
        },
        setOpenTemplate: function(content) {
            openTextController.setContent(content);
        },
        setBodyText: function(content) {
            bodyTextController.setContent(content);
        },
        setCloseTemplate: function(content) {
            closingTextController.setContent(content);
        },
        clearContext : function() {
            for (var i = 0; i < contextFieldCount; i++) {
                contextFields[i].clear();
            }
            contextFieldCount = 0;
        },
        addContextItem : function(enabled, name, value, isNullValue) {
            var contextField = createContextField();
            contextField.setNullValue(isNullValue);
            contextField.setName(name);
            contextField.setValue(value);
            contextField.setEnabled(enabled);
        },
        parseTemplates : function() {
            parseTemplate();
        }
    };
}();

/** Loads an example into the application */
FreemarkerTool.ExampleLoader = function(){

    var ERROR_EVENT = "error";
    var START_LOADING_EVENT = "loadingExample";
    var STOP_LOADING_EVENT = "loadedExample";

    var INDICATOR_ID = "exampleIndicator";
    var EXAMPLE_ID = "examples";
    var EXAMPLE_URL = "/example.json";

    var indicatorController;

    function showIndicator() {
        blueskyminds.events.fire(START_LOADING_EVENT);
    }

    function hideIndicator() {
        blueskyminds.events.fire(STOP_LOADING_EVENT);
    }

    /** private callback for the asyncRequest */
    var exampleCallback = {
        success: function(o) {
            hideIndicator();
            var payload = blueskyminds.net.json.evaluate(o.responseText);
            if (payload) {
                if (payload.exception) {
                    blueskyminds.events.fire(ERROR_EVENT, payload.exception.message);
                } else {
                    var example = payload.example;
                    if (example) {
                        // load the example into the UI
                        FreemarkerTool.ui.setViewMode(example.templateView);
                        FreemarkerTool.ui.setOpenTemplate(example.openTemplate);
                        FreemarkerTool.ui.setBodyText(example.bodyText);
                        FreemarkerTool.ui.setCloseTemplate(example.closeTemplate);
                        FreemarkerTool.ui.clearContext();
                        if (payload.example.context) {
                            for (var i = 0; i < example.context.length; i++) {
                                var contextField = example.context[i];
                                FreemarkerTool.ui.addContextItem(contextField.enabled, contextField.name, contextField.value, contextField.nullValue);
                            }
                        }
                        FreemarkerTool.ui.parseTemplates();
                    } else {
                        blueskyminds.events.fire(ERROR_EVENT, "The requested example couldn't be loaded by the server");
                    }
                }
            } else {
                blueskyminds.events.fire(ERROR_EVENT, "Invalid response received from the server");
            }
        },
        failure: function(o) {
            hideIndicator();
            blueskyminds.events.fire(ERROR_EVENT, blueskyminds.net.errorMessage(o));
        }
    };

    /** Listener for a selection in the example box */
    function onSelection(eventInfo) {
        var el = document.getElementById(EXAMPLE_ID);
        if (el) {
            var index = el.selectedIndex;
            if (index) {
                showIndicator();
                YAHOO.util.Connect.resetFormState();
                YAHOO.util.Connect.asyncRequest('GET', EXAMPLE_URL+"?id="+index, exampleCallback);                                   
            }
        }
    }

    function init() {
        YAHOO.util.Event.addListener(EXAMPLE_ID, 'change', onSelection);

        // setup the connection manager listeners
        blueskyminds.events.register(START_LOADING_EVENT, new YAHOO.util.CustomEvent(START_LOADING_EVENT));
        blueskyminds.events.register(STOP_LOADING_EVENT, new YAHOO.util.CustomEvent(STOP_LOADING_EVENT));

        indicatorController = new blueskyminds.ui.ProgressIndicatorController(INDICATOR_ID, START_LOADING_EVENT, STOP_LOADING_EVENT);
    }

    YAHOO.util.Event.onDOMReady(init);

    return {
        clearSelection : function() {
            var el = document.getElementById(EXAMPLE_ID);
            if (el) {
                el.selectedIndex = 0;
            }
        }
    };
}();

/** Manages the dynamic layout of the page */
FreemarkerTool.layout = function() {

    var CONTEXT_PANEL_ID = "contextPanel";

    var BORDER = 10;
    var HEADER_HEIGHT = 100;
    var CONTENT_VPADDING = BORDER*2;
    var VOVERFLOW = 20;        /** to ensure we don't get scroll bars */
    var FOOTER_HEIGHT = 20;
    var PANEL_MARGIN = BORDER*2+23;
    var SETTINGS_HEIGHT = 68;

    var toggleButton;
    var templateMode;

    function resetHeights() {
        var viewPortHeight = YAHOO.util.Dom.getViewportHeight();
        var availableHeight;
        if (YAHOO.tools.getBrowserEngine().msie) {
            availableHeight = viewPortHeight - HEADER_HEIGHT - (4*BORDER) - FOOTER_HEIGHT - CONTENT_VPADDING - VOVERFLOW;
        } else {
            availableHeight = viewPortHeight - HEADER_HEIGHT - FOOTER_HEIGHT - CONTENT_VPADDING - VOVERFLOW;
        }

        var openHeight;
        var bodyTextHeight;
        var closeHeight;
        var outputHeight;

        if (templateMode) {
            closeHeight = 0;
            bodyTextHeight = 0;
            openHeight = availableHeight / 2;
            outputHeight = availableHeight - openHeight;

            YAHOO.util.Dom.setStyle(FreemarkerTool.constants.BODY_TEXT_CONTAINER_ID, "display", "none");
            YAHOO.util.Dom.setStyle(FreemarkerTool.constants.CLOSE_CONTAINER_ID, "display", "none");

            YAHOO.util.Dom.setStyle(FreemarkerTool.constants.OPEN_TITLE_TAG_ID, "display", "none");
            YAHOO.util.Dom.setStyle(FreemarkerTool.constants.OPEN_TITLE_TEMPLATE_ID, "display", "block");

            document.getElementById(FreemarkerTool.constants.BODY_TEXT_ID).disabled = true;
            document.getElementById(FreemarkerTool.constants.CLOSE_TEXT_ID).disabled = true;

            YAHOO.util.Dom.setStyle(FreemarkerTool.constants.OPEN_CONTAINER_ID, "height", openHeight+"px");
            YAHOO.util.Dom.setStyle(FreemarkerTool.constants.OPEN_TEXT_ID, "height", (openHeight-PANEL_MARGIN)+"px");

            YAHOO.util.Dom.setStyle(FreemarkerTool.constants.OUTPUT_TEXT_CONTAINER_ID, "height", outputHeight+"px");
            YAHOO.util.Dom.setStyle(FreemarkerTool.constants.OUTPUT_TEXT_ID, "height", (outputHeight-PANEL_MARGIN)+"px");
        } else {
            bodyTextHeight = 60;
            closeHeight = 0;
            openHeight = (availableHeight - bodyTextHeight) / 3;
            closeHeight = openHeight;
            outputHeight = availableHeight - openHeight - bodyTextHeight - closeHeight;

            document.getElementById(FreemarkerTool.constants.BODY_TEXT_ID).disabled = false;
            document.getElementById(FreemarkerTool.constants.CLOSE_TEXT_ID).disabled = false;

            YAHOO.util.Dom.setStyle(FreemarkerTool.constants.OPEN_TITLE_TEMPLATE_ID, "display", "none");
            YAHOO.util.Dom.setStyle(FreemarkerTool.constants.OPEN_TITLE_TAG_ID, "display", "block");

            YAHOO.util.Dom.setStyle(FreemarkerTool.constants.OPEN_TEXT_ID, "height", (openHeight-PANEL_MARGIN)+"px");
            YAHOO.util.Dom.setStyle(FreemarkerTool.constants.OPEN_CONTAINER_ID, "height", openHeight+"px");

            YAHOO.util.Dom.setStyle(FreemarkerTool.constants.BODY_TEXT_CONTAINER_ID, "display", "block");
            YAHOO.util.Dom.setStyle(FreemarkerTool.constants.BODY_TEXT_CONTAINER_ID, "height", bodyTextHeight+"px");

            YAHOO.util.Dom.setStyle(FreemarkerTool.constants.CLOSE_TEXT_ID, "height", (closeHeight-PANEL_MARGIN)+"px");
            YAHOO.util.Dom.setStyle(FreemarkerTool.constants.CLOSE_CONTAINER_ID, "display", "block");
            YAHOO.util.Dom.setStyle(FreemarkerTool.constants.CLOSE_CONTAINER_ID, "height", closeHeight+"px");

            YAHOO.util.Dom.setStyle(FreemarkerTool.constants.OUTPUT_TEXT_ID, "height", (outputHeight-PANEL_MARGIN)+"px");
            YAHOO.util.Dom.setStyle(FreemarkerTool.constants.OUTPUT_TEXT_CONTAINER_ID, "height", outputHeight+"px");
        }

        var ePanel = document.getElementById("mainPanel");
        var eSettings = document.getElementById("settings");
        var rightHeight = (ePanel.offsetHeight-CONTENT_VPADDING);
        var contextHeight = rightHeight-eSettings.offsetHeight;
        YAHOO.util.Dom.setStyle("right", "height",  rightHeight+"px");
        YAHOO.util.Dom.setStyle(CONTEXT_PANEL_ID, "height", (contextHeight-PANEL_MARGIN)+"px");
    }

    /** Radio button listener to toggle between the two view modes */
    function toggleView(eventInfo) {
        if (this.get('checkedButton').index === 0) {
            templateMode = true;
        } else {
            templateMode = false;
        }

        resetHeights();
        document.getElementById(FreemarkerTool.constants.OPEN_TEXT_ID).focus();
    }

    function onResizeListener(e) {
        resetHeights();
    }

    function initLayout() {
        // setup the toggle view button
        toggleButton = new YAHOO.widget.ButtonGroup(FreemarkerTool.constants.TOGGLE_VIEW_BTN_ID);

        toggleButton.addListener("checkedButtonChange", toggleView);

        FreemarkerTool.layout.setTemplateView(true);

        YAHOO.util.Event.addListener(window, 'resize', onResizeListener);
    }

    return {
        init : function() {
            initLayout();
        },
        reset : function(){
            resetHeights();
        },
        /** Switches the view between template or tag mode */
        setTemplateView: function(template) {
            if (template) {
                toggleButton.check(0);
            } else {
                toggleButton.check(1);
            }
        }
    };
}();
