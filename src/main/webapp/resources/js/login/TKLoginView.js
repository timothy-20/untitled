// const idInput = document.getElementById("id");
// const passwordInput = document.getElementById("password");
// const resetButton = document.getElementById("reset-button");
// resetButton.onclick = (event) => {
//     event.preventDefault();
//
//     idInput.value = "";
//     passwordInput.value = "";
// };
//
// const loginButton = document.getElementById("login-button");
// loginButton.onclick = (event) => {
//     event.preventDefault();
//
//     (async () => {
//         const id = idInput.value;
//         const password = passwordInput.value;
//         const request = {
//             method: "POST",
//             headers: { "Content-Type": "application/x-www-form-urlencoded" },
//             body: new URLSearchParams({
//                 "id": id,
//                 "password": password
//             })
//         };
//         const response = await fetch("/login/api/user-account", request);
//         const data = await response.json();
//
//         if (!response.ok) {
//             const errorElementIdMap = {
//                 401: "password-error-label",
//                 422: "id-error-label"
//             };
//             const errorElementId = errorElementIdMap[response.status];
//
//             if (errorElementId) {
//                 const errorDiv = document.getElementById(errorElementId);
//                 errorDiv.style.color = "red";
//                 errorDiv.innerHTML = data["message"];
//             }
//
//             return;
//         }
//
//         await fetch(data["redirectUrl"], {
//             method: "GET",
//             redirect: "follow"
//         });
//
//     })().catch(error => {
//         console.log("Fail to receive response for login. ", error);
//     });
// };