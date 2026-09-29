async function deleteYes() {

    document.getElementById("error_message").innerText = "";
    const token = localStorage.getItem("jwt");
    if (!token) {
        console.log("No JWT token found in localStorage.");
        return;
    }

    const response = await fetch("/connect/delete", {
        method: "DELETE",
        headers: {
            "Authorization": `Bearer ${token}`
        }
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
        alert(data.message); //Account deleted
        window.location.href = "/";
        return;
    }

    if (response.status === 409) {
        document.getElementById(`error_message`).innerText = "Could not delete account"; //Could not delete account
        return;
    } else if (response.status === 503) {
        document.getElementById("error_message").innerText = "The server is temporarily down. Please try again later.";
        return;
    }

    if (!response.ok) {
        document.getElementById("error_message").innerText = data.error || "Unexpected error occur";
    }
}

async function deleteNo() {
    window.location.href = "/mypage";
}