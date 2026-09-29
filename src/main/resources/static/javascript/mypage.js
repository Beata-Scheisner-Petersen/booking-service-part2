async function loadMyPage() {

    document.getElementById("error_message").innerText = "";
    const token = localStorage.getItem("jwt");

    const response = await fetch("/connect/info", {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    });

    const data = await response.json();

    if (response.status === 503) {
        document.getElementById("error_message").innerText = "customer-server is temporary down. You may not be able to " +
            "see or change customer info!";
    }
    if (!response.ok) {
        document.getElementById("error_message").innerText = data.error || "Unexpected error occur";
    }

    document.getElementById("firstname").innerText = data.firstname;
    document.getElementById("lastname").innerText = data.lastname;
    document.getElementById("email").innerText = data.email;
    document.getElementById("phone").innerText = data.phoneNumber;
}

document.addEventListener("DOMContentLoaded", loadMyPage);
