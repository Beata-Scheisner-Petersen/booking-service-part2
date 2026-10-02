async function registerCustomer() {

    const firstname = document.getElementById("firstname").value;
    const lastname = document.getElementById("lastname").value;
    const identificationNumber = document.getElementById("id_number").value;
    const email = document.getElementById("email").value;
    const phoneNumber = document.getElementById("phonenumber").value;
    const password = document.getElementById("password").value;

    //Clears old errors
    document.getElementById("firstname_error").innerText = "";
    document.getElementById("lastname_error").innerText = "";
    document.getElementById("identificationNumber_error").innerText = "";
    document.getElementById("email_error").innerText = "";
    document.getElementById("phoneNumber_error").innerText = "";
    document.getElementById("password_error").innerText = "";
    document.getElementById("result_message").innerText="";

    const response = await fetch("/connect/create", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
            firstname,
            lastname,
            identificationNumber,
            email,
            password,
            phoneNumber
        })
    });

    const rawValue = await response.text();

    //Parsing between text and json
    let data;
    try {
        data = JSON.parse(rawValue);
    } catch {
        data = rawValue;
    }
    console.log("data: ", data," type: ", typeof data);

    if (response.ok) {
        window.location.href = "/login";
        return;
    }

    //Validation errors (400)
    if (response.status === 400) {
        if (typeof data === "object") {
            for (const field in data) {
                const errorDiv = document.getElementById(`${field}_error`);
                if (errorDiv) {
                    errorDiv.innerHTML = data[field];
                }
            }
            return;
        } else {
            //handle text 400 errors
            document.getElementById("result_message").innerText = data;
            return;
        }
    } else if (response.status === 409) {

        const errorMessage = typeof data === "object" ? (data.message || JSON.stringify(data)) : data;

        if (errorMessage.includes("Email")) {
            document.getElementById("email_error").innerText = data;
        } else if (errorMessage.includes("Identification")) {
            document.getElementById("identificationNumber_error").innerText = data;
        } else if (errorMessage.includes("Phone")) {
            document.getElementById("phoneNumber_error").innerText = data;
        } else {
            document.getElementById("result_message").innerText = data;
        }
        return;
    } else if (response.status === 503) {
        document.getElementById("result_message").innerText = "The server is temporarily down. Please try again later.";
        return;
    } else if (!response.ok) {
        console.log("!response.ok")
        document.getElementById("result_message").innerText = data.error || "Unexpected error occur";
        return;
    }
}
