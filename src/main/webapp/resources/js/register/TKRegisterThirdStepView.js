// 아이디 중복 체크
let isValidateId = false;
let isValidatePassword = false;
let isConfirmPassword = false;

let idErrorDiv = document.getElementById("id-error-label");
let idInput = document.getElementById("id");
let checkIdDuplicationButton = document.getElementById("check-id-duplication");
checkIdDuplicationButton.addEventListener("focusin", () => {
    idErrorDiv.style.color = "transparent";
    idErrorDiv.innerHTML = "";
});
checkIdDuplicationButton.onclick = () => {
    (async () => {
        idErrorDiv.style.color = "red";

        try {
            const response = await fetch("/register/api/third-step/check-id-duplication", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    id: idInput.value
                })
            });
            const data = await response.json();
            idErrorDiv.innerHTML = data.message;

            if (response.ok) {
                idErrorDiv.style.color = "green";
                idErrorDiv.innerHTML = "사용 가능한 아이디입니다.";
                isValidateId = true;
            }

        } catch (error) {
            console.log(error);
            idErrorDiv.innerHTML = error;
        }
    })()
}


let passwordErrorDiv = document.getElementById("password-error-label");
let passwordInput = document.getElementById("password");
passwordInput.addEventListener("focusin", () => {
    passwordErrorDiv.style.color = "transparent";
    passwordErrorDiv.innerHTML = "";
});
passwordInput.addEventListener("focusout", () => {
    passwordErrorDiv.style.color = "red";
    let password = passwordInput.value;

    if (!(password.length >= 8)) {
        passwordErrorDiv.innerHTML = "패스워드는 8자 이상이여야 합니다.";
        return;
    }

    if (!(/^(?=.*[!@#$%^&*]).{8,}$/.test(password))) {
        // 특수문자 포함 여부 확인
        passwordErrorDiv.innerHTML = "특수문자(!@#$%^&*)가 포함되어야 합니다.";
        return;
    }

    if (!(/^(?=.*[0-9])(?=.*[A-Za-z]).{8,}$/.test(password))) {
        // 숫자 및 알파벳 포함 여부
        passwordErrorDiv.innerHTML = "숫자 및 알파벳 대소문자가 포함되어야 합니다.";
        return;
    }

    passwordErrorDiv.style.color = "green";
    passwordErrorDiv.innerHTML = "유효한 패스워드 입니다.";
    isValidatePassword = true;
});

let passwordConfirmErrorDiv = document.getElementById("password-confirm-error-label");
let passwordConfirmInput = document.getElementById("password-confirm");
passwordConfirmInput.addEventListener("focusin", () => {
    passwordConfirmErrorDiv.style.color = "transparent";
    passwordConfirmErrorDiv.innerHTML = "";
});
passwordConfirmInput.addEventListener("focusout", () => {
    let password = passwordInput.value;
    let passwordConfirm = passwordConfirmInput.value;

    if (passwordConfirm !== password) {
        passwordConfirmErrorDiv.style.color = "red";
        passwordConfirmErrorDiv.innerHTML = "패스워드와 일치하지 않습니다.";
        return;
    }

    passwordConfirmErrorDiv.style.color = "green";
    passwordConfirmErrorDiv.innerHTML = "패스워드와 일치합니다.";
    isConfirmPassword = true;
});

let formElements =  document.getElementsByTagName("form");

for (let i = 0; i < formElements.length; i++) {
    let formElement = formElements[i];

    if (formElement.action.includes("/register/api/third-step")) {
        formElement.onsubmit = () => {
            return isValidateId && isValidatePassword && isConfirmPassword;
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