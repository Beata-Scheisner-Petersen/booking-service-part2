async function getMyReviews() {
    const token = localStorage.getItem("jwt");

    if (!token) {
        console.error("No JWT token found in localStorage.");
        return;
    }

    try {
        const response = await fetch("/reviews/user", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            console.error("Failed to fetch reviews:", response.status, response.statusText);
            return;
        }

        const reviews = await response.json();
        const tableBody = document.getElementById("table_body");

        if (!tableBody) {
            console.error("Table body element with ID 'table_body' was not found.");
            return;
        }

        tableBody.innerHTML = "";

        if (reviews.length === 0) {
            tableBody.innerHTML = `<tr><td colspan="4">No reviews found.</td></tr>`;
            return;
        }

        reviews.forEach((review) => {
            const row = document.createElement("tr");

            const formattedDate = review.creationDate
                ? new Date(review.creationDate).toLocaleDateString()
                : "";

            row.innerHTML = `
                <td>${review.roomNumber ?? ""}</td>
                <td>${review.reviewContent ?? ""}</td>
                <td>${review.reviewScore != null ? review.reviewScore + '/5' : 'N/A'}</td>
                <td>${formattedDate}</td>
            `;

            tableBody.appendChild(row);
        });

    } catch (error) {
        console.error("Error loading reviews:", error);
    }
}

document.addEventListener("DOMContentLoaded", getMyReviews);