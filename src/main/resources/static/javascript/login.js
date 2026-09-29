async function login() {

    //Values from Input
    const email = document.getElementById("email_input").value;
    const password = document.getElementById("password_input").value;

    //Clear old errors
    document.getElementById("error_message").innerText = "";

    const response = await fetch("/connect/login", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({email, password})
    });

    const rawValue = await response.text();

    //Parsing between text and json
    let data;
    try {
        data = JSON.parse(rawValue);
    } catch {
        data = rawValue;
    }

    if (response.ok) {
        localStorage.setItem("jwt", data)
        window.location.href = "/mypage";
        return;
    }

    if (response.status === 503) {
        document.getElementById("error_message").innerText = "The server is temporarily down. Please try again later.";
        return;
    }

    if (response.status === 400) {
       document.getElementById("error_message").innerText = "Wrong email or password, try again"
        return;
    }

    if (!response.ok) {
        document.getElementById("error_message").innerText = data.error || "Unexpected error occur";
    }

    try {
        const errorData = JSON.parse(data);
        document.getElementById("error_message").innerText = errorData.error;
    } catch {
        document.getElementById("error_message").innerText = "unexpected error: " + data;
    }
}

async function registerNewCustomer() {
    window.location.href = "/register";
}