<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<title>login</title>

<style type="text/css">
    form label {
        font-weight: normal;
    }

    form a {
        font-size: 16px;
    }

    #mainPanel {
        width: 400px;
        font-size: 20px;
    }
    #loginForm ul {
        list-style-type: none;
        background-color: #F2DEDE;
        border-color: #EED3D7;
        color: #B94A48;
        border-radius: 4px;
        margin-bottom: 20px;
        padding: 15px;
        text-align: justify;
    }
    .k-widget.k-tooltip-validation {
        font-size: 16px;
    }
    .validation-summary-errors {
        text-align: justify;
        font-size: 16px;
    }
    .validation-summary-errors ul {
        padding-left: 20px;
    }
    #mainPanel .k-button {
        font-size: 17px;
    }
    .capsLock {
        background: none repeat scroll 0 0 #EAC1C7;
        border: 1px solid #EAC1C7;
        border-radius: 0 0 5px 5px;
        box-shadow: 0 0 5px #EAC1C7;
        color: #914545;
        margin: -15px 6% 0;
        text-align: center;
        width: 120px;
        font-size: 16px;
        position: absolute;
    }
</style>

<script type="text/javascript" >
    var validator;
    $(document).ready(function () {
        validator = $("#loginForm").kendoValidator().data("kendoValidator");
        $('#username').focus();
        check_capslock_form($('#loginForm')); //applies the capslock check to all input tags
    });

    document.onkeydown = function (e) { //check if capslock key was pressed in the whole window
        e = e || event;
        if (typeof (window.lastpress) === 'undefined') { window.lastpress = e.timeStamp; }
        if (typeof (window.capsLockEnabled) !== 'undefined') {
            if (e.keyCode === 20 && e.timeStamp > window.lastpress + 50) {
                window.capsLockEnabled = !window.capsLockEnabled;
                $('#capslockdiv').fadeToggle();
            }
            window.lastpress = e.timeStamp;
            //sometimes this function is called twice when pressing capslock once, so I use the timeStamp to fix the problem
        }

    };

    function check_capslock(e) { //check what key was pressed in the form
        var s = String.fromCharCode(e.keyCode);
        if (s.toUpperCase() === s && s.toLowerCase() !== s && !e.shiftKey) {
            window.capsLockEnabled = true;
            $('#capslockdiv').fadeIn();
        }
        else {
            window.capsLockEnabled = false;
        }
    }

    function check_capslock_form(where) {
        if (!where) { where = $(document); }
        where.find('input,select').each(function () {
            if (this.type !== "hidden") {
                $(this).keypress(check_capslock);
            }
        });
    }
</script>