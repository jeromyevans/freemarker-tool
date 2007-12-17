blueskyminds.namespace("FreemarkerTool");

FreemarkerTool.ui = function() {

    var FORM_ID = "templateForm";
    var OPEN_CONTAINER_ID = "openContainer";
    var OPEN_TEXT_ID = "open";
    var CLOSE_CONTAINER_ID = "closeContainer";
    var CLOSE_TEXT_ID = "close";
    var BODY_TEXT_CONTAINER_ID = "bodyTextContainer";
    var BODY_TEXT_ID = "bodyText"
    var OUTPUT_TEXT_ID = "outputText"
    var ERROR_ID = "errorContainer";
    var INDICATOR_ID = "indicator";
    var TOGGLE_VIEW_BTN_ID = "toggleViewBtn";

    /** The number of milliseconds to wait before posting a request */
    var CHANGE_TIMEOUT = 1000;
    var TICK_INTERVAL = 250;

    var INITIAL_CONTEXT_ITEMS = 20;

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
    * @param idleTimer an IdleTimer used to detect when the input has been idle since the last update
    * @param changedCallback callback to notify */
    var TextInputController = function(id, callback) {
        var element;

        function onChangeListener(e) {
            callback();
        }

        element = new YAHOO.util.Element(id);
        element.on('keyup', onChangeListener)
        element.on('keydown', onChangeListener)
        element.on('keypress', onChangeListener)
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

    var connectionEvents = {
        start: function(eventType, args){
            showIndicator();
        },
        complete:function(eventType, args){
            //hideIndicator();
        },
        success:function(eventType, args){
            hideIndicator();
        },
        failure:function(eventType, args){
            hideIndicator();
        },
        abort:function(eventType, args){
            hideIndicator();
        }
    }

    // private callback for the asyncRequest
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
        if (formEl) {
           YAHOO.util.Connect.setForm(formEl);
           showIndicator();
           YAHOO.util.Connect.asyncRequest('POST', formEl.action, parseCallback);
        } else {
            blueskyminds.events.fire(ERROR_EVENT, "Form '"+FORM_ID+"' is missing");
        }
    }

    function setTemplateView(template) {
        if (template) {
            //YAHOO.util.Dom.getViewportHeight();
            YAHOO.util.Dom.setStyle(BODY_TEXT_CONTAINER_ID, "display", "none");
            YAHOO.util.Dom.setStyle(CLOSE_CONTAINER_ID, "display", "none");
            YAHOO.util.Dom.setStyle(OPEN_TEXT_ID, "height", "35em");
        } else {
            YAHOO.util.Dom.setStyle(OPEN_TEXT_ID, "height", "15em");
            YAHOO.util.Dom.setStyle(CLOSE_CONTAINER_ID, "display", "block");
            YAHOO.util.Dom.setStyle(BODY_TEXT_CONTAINER_ID, "display", "block");
        }
    }

    function toggleView(e) {
        if (this.get('checked')) {
            setTemplateView(true);
        } else {
            setTemplateView(false);
        }
    }


    /** A ContextField holds the model for the fields used to define each item in the context */
    var ContextField = function(fieldIndex) {

        var CONTEXT_CONTAINER_ID = "context[{0}].container";
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

        var lastValue = "";

        /** Merges the index into a template used to determine the ID */
        function idOf(template) {
            return YAHOO.Tools.printf(template, index);
        }

        function onEnabledChange(e) {
            if (e.target.checked) {
                enable();
            } else {
                disable();
            }
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

        function onNameChange(e) {
            enable();
        }

        function onNameFocus(e) {
            enable();
        }

        function onValueChange(e) {
            enable();
        }

        function onValueFocus(e) {
            enable();
        }

        function onNullChange(e) {
            setNullValue(e.target.checked);
            enable();
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
            nameEl = new YAHOO.util.Element(idOf(CONTEXT_NAME_TEXT_ID));
            valueEl = new YAHOO.util.Element(idOf(CONTEXT_VALUE_TEXT_ID));
            nullEl = new YAHOO.util.Element(idOf(CONTEXT_VALUE_NULL_CHECKBOX_ID));

            if (enabledEl) {
                enabledEl.on('click', onEnabledChange);
            }

            if (nameEl) {
                nameEl.on('keyup', onNameChange)
                nameEl.on('keydown', onNameChange)
                nameEl.on('keypress', onNameChange)
                nameEl.on('focus', onNameFocus)
                nameEl.on('click', onNameFocus)
            }

            if (valueEl) {
                valueEl.on('keyup', onValueChange)
                valueEl.on('keydown', onValueChange)
                valueEl.on('keypress', onValueChange)
                valueEl.on('focus', onValueFocus)
                valueEl.on('click', onValueFocus)
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
            }
        }
    };

    /**
     * Setup the UI controls to define the context
     */
    function initContext() {
        contextFields = new Array();
        for (var itemNo = 0; itemNo < INITIAL_CONTEXT_ITEMS; itemNo++) {
            contextFields[itemNo] = new ContextField(itemNo);
        }
    }

    function init() {
        errorController = new blueskyminds.ui.ErrorController(ERROR_ID, ERROR_EVENT)

        // the idle timer waits for idle input before posting changes
        idleTimer = new IdleTimer(TICK_INTERVAL, CHANGE_TIMEOUT, idleCallback)

        // controllers for each of the widgets
        openTextController = new TextInputController(OPEN_TEXT_ID, inputChangedListener);
        bodyTextController = new TextInputController(BODY_TEXT_ID, inputChangedListener);
        closingTextController = new TextInputController(CLOSE_TEXT_ID, inputChangedListener);

        // setup the connection manager listeners
        blueskyminds.events.register("startedParsing", new YAHOO.util.CustomEvent("startedParsing"));
        blueskyminds.events.register("stoppedParsing", new YAHOO.util.CustomEvent("stoppedParsing"));

        indicatorController = new blueskyminds.ui.ProgressIndicatorController(INDICATOR_ID, "startedParsing", "stoppedParsing");

        // setup the toggle view button
        var toggleButton = new YAHOO.widget.Button(TOGGLE_VIEW_BTN_ID);

        toggleButton.on("click", toggleView);
        setTemplateView(toggleButton.get('checked'));

        initContext();
     }


    init();

    return {
        parseTemplate : function() {

        }
    }
}();