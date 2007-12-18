blueskyminds.namespace("FreemarkerTool");

FreemarkerTool.ui = function() {

    var FORM_ID = "templateForm";
    var OPEN_CONTAINER_ID = "openContainer";
    var OPEN_CONTAINER_BOX_ID = "openContainerBox";
    var OPEN_TEXT_ID = "open";
    var CLOSE_CONTAINER_ID = "closeContainer";
    var CLOSE_CONTAINER_BOX_ID = "closeContainerBox";
    var CLOSE_TEXT_ID = "close";
    var BODY_TEXT_CONTAINER_ID = "bodyTextContainer";
    var BODY_TEXT_CONTAINER_BOX_ID = "bodyTextContainerBox";
    var BODY_TEXT_ID = "bodyText"
    var OUTPUT_TEXT_CONTAINER_ID = "outputTextContainer"
    var OUTPUT_TEXT_ID = "outputText"
    var ERROR_ID = "errorContainer";
    var INDICATOR_ID = "indicator";
    var TOGGLE_VIEW_BTN_ID = "toggleViewBtnGroup";
    var OPEN_TITLE_TEMPLATE_ID = "openTitleTemplate";
    var OPEN_TITLE_TAG_ID = "openTitleTag";

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

    var templateMode;

    /** Calls the callback if not reset for the specifid number of ticks to wait */
    var IdleTimer = function(tickInterval, millisToWait, callback) {

        var tickCounter;
        var lastFired;
        var ticksToWait;

        function init() {
            tickCounter = 0;
            lastFired = 0;
            ticksToWait = (millisToWait / tickInterval);
            setInterval(tickListener, tickInterval);
        }

        /** Executed every interval tick */
        function tickListener() {
            tickCounter++;
            if (tickCounter-lastFired > ticksToWait) {
                callback();
                lastFired = tickCounter;
            }
        }

        init();

        return {
            reset : function() {
                tickCounter = 0;
                lastFired = 0;
            }
        }
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
        element.on('blur', onBlurListener)

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
        }
    };   

    /** Updates the value displayed in the ouput */
    function setFreemarkerOutput(text) {
        var el = document.getElementById(OUTPUT_TEXT_ID);
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

    /** When idle, check if the input has changed, and if so parse the template */
    function idleCallback() {
        if (isInputChanged()) {
            clearInputChangedFlag();
            parseTemplate();
        }
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
            var payload = blueskyminds.net.json.eval(o.responseText);
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
        var formEl = document.getElementById(FORM_ID);

        FreemarkerTool.ui.dismissErrors();
        
        if (formEl) {
           YAHOO.util.Connect.setForm(formEl);
           showIndicator();
           YAHOO.util.Connect.asyncRequest('POST', formEl.action, parseCallback);
        } else {
            blueskyminds.events.fire(ERROR_EVENT, "Form '"+FORM_ID+"' is missing");
        }
    }

    /** Switches the view between template or tag mode */
    function setTemplateView(template) {
        if (template) {
            templateMode = true;
            YAHOO.util.Dom.setStyle(BODY_TEXT_CONTAINER_ID, "display", "none");
            YAHOO.util.Dom.setStyle(CLOSE_CONTAINER_ID, "display", "none");
            YAHOO.util.Dom.setStyle(OPEN_TITLE_TAG_ID, "display", "none");
            YAHOO.util.Dom.setStyle(OPEN_TITLE_TEMPLATE_ID, "display", "block");
            document.getElementById(BODY_TEXT_ID).disabled = true;
            document.getElementById(CLOSE_TEXT_ID).disabled = true;
        } else {
            templateMode = false;
            document.getElementById(BODY_TEXT_ID).disabled = false;
            document.getElementById(CLOSE_TEXT_ID).disabled = false;
            YAHOO.util.Dom.setStyle(OPEN_TITLE_TEMPLATE_ID, "display", "none");
            YAHOO.util.Dom.setStyle(OPEN_TITLE_TAG_ID, "display", "block");
            YAHOO.util.Dom.setStyle(BODY_TEXT_CONTAINER_ID, "display", "block");
            YAHOO.util.Dom.setStyle(CLOSE_CONTAINER_ID, "display", "block");
        }
        resetHeights();        
    }

    /** Radio button listener to toggle between the two view modes */
    function toggleView(eventInfo) {
        if (this.get('checkedButton').index == 0) {
            setTemplateView(true);
        } else {
            setTemplateView(false);
        }
        document.getElementById(OPEN_TEXT_ID).focus();       
    }

    var CONTEXT_CONTAINER_ID = "context[{0}].container";
    var PARENT_CONTEXT_CONTAINER_ID = "contextContainer";
    var CONTEXT_TEMPLATE_ID = "contextFieldTemplate";
    var contextFieldTemplate;
    var contextFieldCount;

    /** A ContextField holds the model for the fields used to define each item in the context
    * @param fieldIndex sequential number of this field
    * @param listener to fire when focus is obtained
    **/
    var ContextField = function(fieldIndex, onFocusListener, onChangeListener) {

        var CONTEXT_ENABLED_CHECKBOX_ID = "context[{0}].enabled";
        var CONTEXT_NAME_TEXT_ID = "context[{0}].name";
        var CONTEXT_VALUE_TEXT_ID = "context[{0}].value";
        var CONTEXT_VALUE_NULL_CHECKBOX_ID = "context[{0}].nullValue";

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

        function isEnabled() {
            return enabledEl.get('checked');
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
                if (lastValue == null) {
                    var value = valueEl.get('element').value;
                    lastValue = (value ? value : "");
                    valueEl.set('value', "< null >");
                }
            } else {
                valueEl.removeClass('disabled');
                if (lastValue != null) {
                   // restore last value
                   valueEl.set('value', lastValue);
                   lastValue = null;
                }
            }
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
        }
    };

    /**
     * Adds a new ContextField to the DOM and initialise a controller for it
     * If the ContxtField already exists its cleared
     * @param itemNo
     * @return contextField instance
     */
    function createContextField() {

        var templateContext = {
            index : contextFieldCount
        }

        var nodeId = YAHOO.tools.printf(CONTEXT_CONTAINER_ID, templateContext.index);
        var node = document.getElementById(nodeId);

        if (!node) {
            // node needs to be created exists
            node  = document.createElement("tr");
            node['id'] = nodeId;
            node['className'] = "contextField";

            var parent = document.getElementById(PARENT_CONTEXT_CONTAINER_ID);
            parent.appendChild(node);

            // insert the HTML into the DOM
            node.innerHTML = contextFieldTemplate.process(templateContext);

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

    /**
     * Setup the UI controls to define the context
     */
    function initContext() {
        contextFields = new Array();
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

    var CONTEXT_PANEL_ID = "contextPanel";
    
    function initLayout() {
        resetHeights();

        YAHOO.util.Event.addListener(window, 'resize', onResizeListener);
    }

    function onResizeListener(e) {
        resetHeights();
    }

    var BORDER = 10;
    var HEADER_HEIGHT = 100;
    var CONTENT_VPADDING = BORDER*2;
    var VOVERFLOW = 20;        /** to ensure we don't get scroll bars */
    var FOOTER_HEIGHT = 20;
    var PANEL_MARGIN = BORDER*2+23;
    var SETTINGS_HEIGHT = 68;

    function resetHeights() {
        var viewPortHeight = YAHOO.util.Dom.getViewportHeight();
        //var viewPortWidth = YAHOO.util.Dom.getViewportWidth();
        var availableHeight = viewPortHeight - HEADER_HEIGHT - FOOTER_HEIGHT - CONTENT_VPADDING - VOVERFLOW;

        var openHeight;
        var bodyTextHeight;
        var closeHeight;
        var outputHeight;

        if (templateMode) {
            closeHeight = 0;
            bodyTextHeight = 0;
            openHeight = availableHeight / 2;
            outputHeight = availableHeight - openHeight;

            YAHOO.util.Dom.setStyle(OPEN_CONTAINER_ID, "height", openHeight+"px");
            YAHOO.util.Dom.setStyle(OUTPUT_TEXT_CONTAINER_ID, "height", outputHeight+"px");

            YAHOO.util.Dom.setStyle(OPEN_TEXT_ID, "height", (openHeight-PANEL_MARGIN)+"px");
            YAHOO.util.Dom.setStyle(OUTPUT_TEXT_ID, "height", (outputHeight-PANEL_MARGIN)+"px");
        } else {
            bodyTextHeight = 60;
            closeHeight = 0;
            openHeight = (availableHeight - bodyTextHeight) / 3;
            closeHeight = openHeight;
            outputHeight = availableHeight - openHeight - bodyTextHeight - closeHeight;

            YAHOO.util.Dom.setStyle(OPEN_CONTAINER_ID, "height", openHeight+"px");
            YAHOO.util.Dom.setStyle(BODY_TEXT_CONTAINER_ID, "height", bodyTextHeight+"px");
            YAHOO.util.Dom.setStyle(CLOSE_CONTAINER_ID, "height", closeHeight+"px");
            YAHOO.util.Dom.setStyle(OUTPUT_TEXT_CONTAINER_ID, "height", outputHeight+"px");

            YAHOO.util.Dom.setStyle(OPEN_TEXT_ID, "height", (openHeight-PANEL_MARGIN)+"px");
            YAHOO.util.Dom.setStyle(CLOSE_TEXT_ID, "height", (closeHeight-PANEL_MARGIN)+"px");
            YAHOO.util.Dom.setStyle(OUTPUT_TEXT_ID, "height", (outputHeight-PANEL_MARGIN)+"px");
        }

        YAHOO.util.Dom.setStyle("right", "height", availableHeight+"px");
        YAHOO.util.Dom.setStyle(CONTEXT_PANEL_ID, "height", (availableHeight-PANEL_MARGIN)+"px");
        //YAHOO.util.Dom.setStyle("center", "height", availableHeight);
    }

    var ERROR_TEMPLATE_ID = "errorTemplate";

    function init() {
        initLayout();

        var errorTemplate = TrimPath.parseDOMTemplate(ERROR_TEMPLATE_ID);

        // the error controller reports errors
        errorController = new blueskyminds.ui.ErrorController(ERROR_ID, ERROR_EVENT, errorTemplate);

        // the idle timer waits for idle input before posting changes
        idleTimer = new IdleTimer(TICK_INTERVAL, CHANGE_TIMEOUT, idleCallback)

        // controllers for each of the widgets
        openTextController = new TextInputController(OPEN_TEXT_ID, OPEN_CONTAINER_BOX_ID, inputChangedListener);
        bodyTextController = new TextInputController(BODY_TEXT_ID, BODY_TEXT_CONTAINER_BOX_ID, inputChangedListener);
        closingTextController = new TextInputController(CLOSE_TEXT_ID, CLOSE_CONTAINER_BOX_ID, inputChangedListener);

        // setup the connection manager listeners
        blueskyminds.events.register("startedParsing", new YAHOO.util.CustomEvent("startedParsing"));
        blueskyminds.events.register("stoppedParsing", new YAHOO.util.CustomEvent("stoppedParsing"));

        indicatorController = new blueskyminds.ui.ProgressIndicatorController(INDICATOR_ID, "startedParsing", "stoppedParsing");

        // setup the toggle view button
        //var toggleButton = new YAHOO.widget.Button(TOGGLE_VIEW_BTN_ID);
        var toggleButton = new YAHOO.widget.ButtonGroup(TOGGLE_VIEW_BTN_ID);

        toggleButton.check(0);
        toggleButton.addListener("checkedButtonChange", toggleView);
        
        setTemplateView(true);

        initContext();

        var openText = document.getElementById(OPEN_TEXT_ID);
        openText.select();
        openText.focus();
    }

    YAHOO.util.Event.onDOMReady(init);

    return {
        dismissErrors : function() {
            errorController.dismissErrors();
        },
        setViewMode: function(template) {
            setTemplateView(template);
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
    }
}();

/** Loads an example into the application */
FreemarkerTool.ExampleLoader = function(){

    var ERROR_EVENT = "error";
    var START_LOADING_EVENT = "loadingExample";
    var STOP_LOADING_EVENT = "loadedExample";

    var INDICATOR_ID = "exampleIndicator"
    var EXAMPLE_ID = "examples";
    var EXAMPLE_URL = "/example.json";

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
            var payload = blueskyminds.net.json.eval(o.responseText);
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
        YAHOO.util.Event.addListener(EXAMPLE_ID, 'change', onSelection)

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
    }
}();