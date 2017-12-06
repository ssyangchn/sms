$(function(){
    function getContextPath() {
        var pathName = document.location.pathname;
        var index = pathName.substr(1).indexOf("/");
        var result = pathName.substr(0,index+1);
        return result;
    }
    function successMsg(msg) {
        $('.msg').text(msg);
        $('#start').prepend($('#success').clone());
    }
    function errorMsg(msg) {
        $('.msg').text(msg);
        $('#start').prepend($('#error').clone());
    }
    $('#addVar').click(function () {
        var label = $('#varLabel');
        var order = parseInt(label.attr('order'));
        label.text("变量"+order);
        order++;
        label.attr('order',order);
        $('#varDiv').clone().insertBefore($('#addVar'));
    });
    
    $('#send').click(function () {
        $.ajax({
            type: 'post',
            url: getContextPath()+"/"+"sms/doSend",
            data: $("form").serialize(),
            success: function(data) {
                if (data.flag) {
                    successMsg(data.msg);
                } else {
                    errorMsg(data.msg);
                }
            }
        });
    });
})