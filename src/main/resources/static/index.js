$(function(){
    $("[data-toggle='tooltip']").tooltip();

    function getContextPath() {
        var pathName = document.location.pathname;
        var index = pathName.substr(1).indexOf("/");
        var result = pathName.substr(0,index+1);
        return result;
    }
    function successMsg(msg) {
        var tmpl = "<div class=\"alert alert-success alert-dismissible fade in msg-div\" role=\"alert\">\n" +
            "        <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>\n" +
            "        <strong>成功！</strong>"+msg+"\n" +
            "    </div>";
        $('#msgBox').prepend(tmpl);
    }
    function errorMsg(msg) {

        var tmpl = "<div id=\"error\" class=\"alert alert-danger alert-dismissible fade in msg-div\" role=\"alert\">\n" +
            "        <button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>\n" +
            "        <strong>失败！</strong> "+msg+"\n" +
            "    </div>";
        $('#msgBox').prepend(tmpl);
    }

    $('#addVar').click(function () {
        var div= $("[name='vars']:last").parent();
        var order = 1;
        if (div.length > 0) {
            order=div.attr('order')-0+1;
        }
        var tmpl = "<div class=\"form-group\" id=\"varDiv\" order=\""+order+"\">\n" +
            "        <label id=\"varLabel\">"+'变量'+order+"</label>\n" +
            "        <input type=\"text\" class=\"form-control\" name=\"vars\" placeholder=\"\"/>\n" +
            "    </div>";

         $(this).parent().before(tmpl);
    });
    $('#delVar').click(function () {
        $("[name='vars']:last").parent().remove();
    });
    $('#clearPhone').click(function () {
        $('#phone').val("");
        $('#count').text(0);
    });
    $('#formatPhone').click(function () {
        var phone =  $.trim($('#phone').val());
        if (phone) {
            var phones = phone.split('\n');
            var temp=[];
            $.each(phones,function(index,value){
                value = $.trim(value);
                if (value&&value != '\n'&&value != '') {
                    if ($.inArray(value, temp) == -1) {
                        temp.push(value);
                    }
                }
            });
            $('#phone').val(temp.join('\n'));
            $('#count').text(temp.length);
        }
    });
    
    $('#send').click(function () {
        $.ajax({
            type: 'post',
            url: getContextPath()+"/"+"sms/send",
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