// Example starter JavaScript for disabling form submissions if there are invalid fields
(function () {
    'use strict'

    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    var forms = document.querySelectorAll('.needs-validation')

    // Loop over them and prevent submission
    Array.prototype.slice.call(forms)
        .forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault()
                    event.stopPropagation()

                }

                form.classList.add('was-validated')
            }, false)
        })


})()


function formBtn(form) {
    'use strict'
    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    var forms = $(form);
    var check;
    var checkValidity = forms.find('.summernote').summernote('isEmpty');
    // Loop over them and prevent submission
    Array.prototype.slice.call(forms)
        .forEach(function (form) {
          if (!form.checkValidity()||checkValidity) {
              alert('필드를 모두입력해주세요');
            check = 1;
          } else {
            check= 2;
          }
            form.classList.add('was-validated');
        })
  return check;
}


function checkForm(form) {
    'use strict'
    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    var forms = $(form);
    var check;
    // Loop over them and prevent submission
    Array.prototype.slice.call(forms)
        .forEach(function (form) {
            if (!form.checkValidity()) {
                check = 1;
            } else {
                check= 2;
            }
            form.classList.add('was-validated');
        })
    return check;
}


function fileCheck(el) {
    if (!/\.(jpeg|jpg|png|bmp)$/i.test(el.value)) {
        alert('이미지 파일만 업로드 가능합니다.');
        el.value = '';
        el.focus();
    }
}


function starFormCheck(form) {
    let starForm = $(form).find('input:radio[name=star]').length;
    let star = $(form).find('input:radio[name=star]').is(':checked');
    let text = $(form).find('.form-control').val();
    let textBoolean;
    // 미입력 또는 공백 입력 방지
    if (text.replace(/\s|　/gi, "").length == 0) {
        $("#modal-message-text").focus();
        textBoolean = false;
    } else {
        textBoolean = true;
    }


    if (starForm == 0) {
        return textBoolean;
    } else {
        return star && textBoolean;
    }


}




