document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("jwt");
    fetch("/api/reservation/getAllCustomerReservation", {
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Could not fetch reservations");
            }
            return response.json();
        })
        .then(data => renderReservations(data))
        .catch(error => console.error("Error:", error));
});

function renderReservations(reservations) {
    const tbody = document.querySelector("#reservationTable tbody");
    tbody.innerHTML = "";

    reservations.forEach(res => {
        const row = document.createElement("tr");

        // Safe boolean check for CANCELED or CANCELLED status
        const currentStatus = res.status ? res.status.toUpperCase() : "";
        const isCanceled = currentStatus === "CANCELED" || currentStatus === "CANCELLED";
        const roomNumber = res.roomNumber;

        row.innerHTML = `
            <td>${res.checkIn}</td>
            <td>${res.checkOut}</td>
            <td>${res.roomNumber}</td>
            <td>${res.extraBed ? "Yes" : "No"}</td>
            <td>${res.totalCost} kr</td>
            <td class="status-${currentStatus.toLowerCase()}">${res.status}</td>
            <td>
                ${isCanceled ? `
                    <button class="action-btn review-btn" onclick="openReviewModal(${roomNumber})">Make Review</button>
                ` : `
                    <button class="action-btn edit-btn" onclick="toggleEditForm(${res.id})">Edit</button>
                    <button class="action-btn delete-btn" onclick="deleteReservation(${res.id}, this)">Delete</button>
                `}
            </td>
        `;

        tbody.appendChild(row);

        // EDIT FORM ROW (only generated for non-canceled bookings)
        if (!isCanceled) {
            const formRow = document.createElement("tr");
            formRow.innerHTML = `
            <td colspan="7">
                <div id="edit-form-${res.id}" class="edit-form" style="display:none;">
                    <h4 id="edit-title">Edit reservation</h4>
                    <div class="edit-grid">
                        <div class="date-block">
                            <label>Check-in date:</label>
                            <input type="date" id="checkIn-${res.id}" value="${res.checkIn}">
                        </div>
                        <div class="date-block">
                            <label>Check-out date:</label>
                            <input type="date" id="checkOut-${res.id}" value="${res.checkOut}">
                        </div> 
                    </div>
                    <button class="action-btn save-btn" onclick="saveChanges(${res.id})">Save changes</button>
                </div>
            </td>
            `;

            tbody.appendChild(formRow);
        }
    });
}

function openReviewModal(roomNumber) {
    document.getElementById("reviewRoomNumber").value = roomNumber;
    document.getElementById("reviewContent").value = "";
    document.getElementById("reviewScore").value = "5";

    const modalEl = document.getElementById("reviewModal");
    if (!modalEl) {
        console.error("Modal element #reviewModal not found in DOM");
        return;
    }

    let modal = bootstrap.Modal.getInstance(modalEl);
    if (!modal) {
        modal = new bootstrap.Modal(modalEl);
    }

    modal.show();
}

async function submitReview() {
    const roomNumber = document.getElementById("reviewRoomNumber").value;
    const reviewContent = document.getElementById("reviewContent").value;
    const reviewScore = document.getElementById("reviewScore").value;
    const token = localStorage.getItem("jwt");

    if (!reviewContent.trim()) {
        alert("Please write a review before submitting.");
        return;
    }

    const payload = {
        roomNumber: parseInt(roomNumber),
        reviewContent: reviewContent,
        reviewScore: parseInt(reviewScore)
    };

    try {
        const response = await fetch("/reviews", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const errorText = await response.text();
            alert(`Failed to submit review: ${errorText}`);
            return;
        }

        // Close modal
        const modalElement = document.getElementById("reviewModal");
        const modalInstance = bootstrap.Modal.getInstance(modalElement);
        if (modalInstance) modalInstance.hide();

        alert("Thank you! Your review has been submitted.");

    } catch (error) {
        console.error("Error submitting review:", error);
        alert("Something went wrong while submitting your review.");
    }
}

// SHOW/HIDE EDIT FORM
function toggleEditForm(id) {
    const form = document.getElementById(`edit-form-${id}`);
    form.style.display = form.style.display === "block" ? "none" : "block";
}

// SAVE CHANGES (PUT)
function saveChanges(id) {
    const checkIn = document.getElementById(`checkIn-${id}`).value;
    const checkOut = document.getElementById(`checkOut-${id}`).value;

    const body = {
        checkIn,
        checkOut
    };

    const token = localStorage.getItem("jwt");
    fetch(`/api/reservation/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(body)
    })
        .then(res => {
            if (!res.ok) {
                throw new Error("Could not update reservation");
            }
            alert("Reservation updated successfully");
            location.reload();
        })
        .catch(err => {
            document.getElementById("errorPopup").style.display = "flex";
            console.error(err);
        });
}

// DELETE RESERVATION
function deleteReservation(id, btn) {
    if (!confirm("Are you sure you want to delete this reservation?")) {
        return;
    }

    const token = localStorage.getItem("jwt");
    fetch(`/api/reservation/${id}`, {
        method: "DELETE",
        headers: {
            "Authorization": `Bearer ${token}`
        }
    })
        .then(res => {
            if (!res.ok) {
                throw new Error("Could not delete reservation");
            }

            btn.closest("tr").nextSibling.remove();
            btn.closest("tr").remove();
            alert("Reservation updated successfully");
            location.reload();
        })
        .catch(err => console.error(err));
}

// CLOSE ERROR POPUP
document.getElementById("closeErrorPopup").addEventListener("click", () => {
    document.getElementById("errorPopup").style.display = "none";
});

