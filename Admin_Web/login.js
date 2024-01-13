let userName = document.querySelector(".username");
let password = document.querySelector(".pwd");
let submitBtn = document.querySelector(".submit-btn");
let errMsg = document.querySelector("#err-msg");
console.log(submitBtn);
submitBtn.onclick = function handleSubmit(){
    if(userName.value ===""){
        userName.style.border = "1px solid red";
        errMsg.innerText = "Vui lòng nhập tài khoản";
        errMsg.style.color = "red";
        return false;
        
    }else if(password.value===""){
        password.style.border = "1px solid red";
        errMsg.innerText = "Vui lòng nhập mật khẩu";
        errMsg.style.color = "red";
        return false;
    }
    else if(password.value!=="admin"){
        password.style.border = "1px solid red";
        errMsg.innerText = "Mật khẩu của bạn sai";
        errMsg.style.color = "red";
        return false;
    }else if(password.value==="admin" && userName.value==="admin"){
        // window.location.href = "127.0.0.1:5500/index.html";
       return true;

    }
   return true;
}

let navLinks = document.getElementById("nav-links");
function showMenu() {
    navLinks.style.right = "0";
}

function hideMenu() {
    navLinks.style.right = "-400px";
}