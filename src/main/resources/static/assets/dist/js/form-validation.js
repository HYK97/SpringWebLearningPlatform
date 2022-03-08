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
function fileCheck(el) {
  if(!/\.(jpeg|jpg|png|bmp)$/i.test(el.value)){
    alert('이미지 파일만 업로드 가능합니다.');
    el.value = '';
    el.focus();
  }
}


function starFormCheck(form) {

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

    return star && textBoolean;




}





