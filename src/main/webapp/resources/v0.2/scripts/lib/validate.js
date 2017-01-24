var availability = {

    cache: {},

    check: function (element, settings, validator) {
        var id = element.attr('id');
        var cache = this.cache[id] = this.cache[id] || {};
        var msg = settings.message;
        element.attr('data-available-msg', dictionary.Checking);

        $.ajax({
            type: "POST",
            url: settings.url,
            dataType: 'json',
            data: { value: element.val() },
            success: function (data) {
                cache.value = element.val();
                cache.valid = data;
                element.attr('data-available-msg', msg);
                validator.validateInput(element);
            },
            failure: function () {
                cache.valid = true;
                element.attr('data-available-msg', msg);
            }
        });
    }
};


function addAvailableRule(currentValue) {
    validator = $(".k-popup-edit-form").data('kendoValidator');
    $(".k-popup-edit-form").data('kendoValidator').options.rules.available = function (input) {
        if (input.val() == currentValue)
            return true;

        var validate = input.data('available');

        if (typeof validate !== 'undefined' && validate !== false) {
            var id = input.attr('id');
            var cache = availability.cache[id] = availability.cache[id] || {};

            cache.checking = true;

            var settings = {
                url: input.data('availableUrl') || '',
                message: input.data('availableMsg') || ''
            };

            if (cache.value === input.val()) {
                if (cache.valid) {
                    // the value is available
                    return true;
                }
                else {
                    // the value is not available
                    cache.checking = false;
                    return false;
                }
            }

            availability.check(input, settings, validator);
            return true;
        }

        // this rule does not apply to this field
        return true;
    }
}