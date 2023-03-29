$(document).ready(function () {

    let $dynamic_div = $('.update-exams');

    $dynamic_div.on('click', 'button[data-update-exams-url]', function () {
        let url = $(this).data('update-exams-url');

        let formData = $('form').serializeArray();

        let val = $(this).val();
        if (val != null) {
            let attrName = $(this).attr('name');
            formData.push({name: attrName, value: val});
        }

        $.post(url, formData, function (data) {
            console.log(data);
            $dynamic_div.find('#tblExams').html(data);
        });

        //$('#tblExams').load(url, formData);
    });

    $dynamic_div.on('change', '.custom-file-input', function () {
        let fileName = $(this).val().split('\\').pop();
        $(this).next('.custom-file-label').addClass("selected").html(fileName);
    });

});
