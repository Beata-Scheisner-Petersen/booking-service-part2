document.addEventListener("DOMContentLoaded", () => {

    const form = document.getElementById("update");

    form.addEventListener("submit", async function (event) {
        event.preventDefault(); // stoppar vanlig submit

        document.getElementById("emailError").textContent = "";
        document.getElementById("phoneError").textContent = "";
        document.getElementById("error_message").innerText = "";

        const formData = new FormData(form);
        const token = localStorage.getItem("jwt");
        const data = Object.fromEntries(formData);

        const response = await fetch("/connect/update", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(data)
        });

        if (response.status === 401) {
            window.location.href = "/login";
            return;
        }
        if (response.status === 503) {
            document.getElementById("error_message").innerText = "The server is temporarily down. Please try again later.";
            return;
        }

        const responseData = await response.json();

        if (responseData.emailError) {
            document.getElementById("emailError").textContent = responseData.emailError;
            return;
        }

        if (responseData.phoneError) {
            document.getElementById("phoneError").textContent = responseData.phoneError;
            return;
        }

        if (!response.ok) {
            document.getElementById("error_message").innerText = data.error || "Unexpected error occur";
            return;
        }

        if (responseData.success) {
            window.location.href = "/mypage";
        }
    });
});
