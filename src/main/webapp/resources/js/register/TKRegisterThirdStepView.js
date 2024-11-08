function validatePassword() {
    let errorLabelElement = document.getElementById("password-error-label");
    errorLabelElement.style.color = "red";
    let password = document.getElementById("password").value;

    if (!(password.length >= 8)) {
        errorLabelElement.innerHTML = "패스워드는 8자 이상이여야 합니다.";
        return false;
    }

    if (!(/^(?=.*[!@#$%^&*]).{8,}$/.test(password))) {
        // 특수문자 포함 여부 확인
        errorLabelElement.innerHTML = "특수문자(!@#$%^&*)가 포함되어야 합니다.";
        return false;
    }

    if (!(/^(?=.*[0-9])(?=.*[A-Za-z]).{8,}$/.test(password))) {
        // 숫자 및 알파벳 포함 여부
        errorLabelElement.innerHTML = "숫자 및 알파벳 대소문자가 포함되어야 합니다.";
        return false;
    }

    errorLabelElement.style.color = "green";
    errorLabelElement.innerHTML = "유효한 패스워드 입니다.";
    return true;
}

function validateConfirmPassword() {
    let errorLabelElement = document.getElementById("password-confirm-error-label");
    let password = document.getElementById("password").value;
    let passwordConfirm = document.getElementById("password-confirm").value;

    if (passwordConfirm === password) {
        errorLabelElement.style.color = "green";
        errorLabelElement.innerHTML = "패스워드와 일치합니다."
        return true;

    } else {
        errorLabelElement.style.color = "red";
        errorLabelElement.innerHTML = "패스워드와 일치하지 않습니다.";
        return false;
    }
}

let formElements =  document.getElementsByTagName("form");

for (let i = 0; i < formElements.length; i++) {
    let formElement = formElements[i];

    if (formElement.action.includes("/register/api/third-step")) {
        formElement.onsubmit = () => {
            // 에러 라벨 상태 초기화
            let errorLabelElement = document.getElementById("password-error-label");
            errorLabelElement.innerHTML = "";
            errorLabelElement.style.color = "transparent";

            errorLabelElement = document.getElementById("password-confirm-error-label");
            errorLabelElement.innerHTML = "";
            errorLabelElement.style.color = "transparent";

            if (!validatePassword()) {
                return false;
            }

            return validateConfirmPassword();
        };
    }
}

// 패스워드 입력 내용 숨기기 및 보여주기
let showPasswordsElement = document.getElementById("show-passwords");
showPasswordsElement.onchange = () => {
    let passwordElement = document.getElementById("password");
    let passwordConfirmElement = document.getElementById("password-confirm");

    if (showPasswordsElement.checked) {
        passwordElement.type = "text";
        passwordConfirmElement.type = "text";
    } else {
        passwordElement.type = "password";
        passwordConfirmElement.type = "password";
    }
};

// 아이디 중복 체크
let checkIdDuplicationElement = document.getElementById("check-id-duplication");
checkIdDuplicationElement.onclick = () => {
    let idElement = document.getElementById("id");
    let idErrorElement = document.getElementById("id-error-label");

    (async () => {
        try {
            const response = await fetch("/register/api/third-step/check-id-duplication", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    id: idElement.value
                })
            });
            const data = await response.json();
            idErrorElement.innerText = data.message;

            if (response.ok) {
                idErrorElement.style.color = "green";

            } else {
                idErrorElement.style.color = "red";
            }

        } catch (error) {
            console.log(error);
            idErrorElement.innerText = error;
            idErrorElement.style.color = "red";
        }
    })()
}