async function getAvailableRooms() {
    const checkIn = document.getElementById('checkIn').value;
    const checkOut = document.getElementById('checkOut').value;
    const guests = document.getElementById('guests').value;

    if (!checkIn || !checkOut || !guests) {
        alert('Please fill in all fields.');
        return;
    }

    const params = new URLSearchParams({ checkIn, checkOut, guests });

    try {
        const response = await fetch(`api/reservation?${params}`);

        if (!response.ok) {
            console.error('Failed to fetch rooms');
            return;
        }

        const rooms = await response.json();

        renderRooms(rooms);

        loadRoomRatings(rooms);

    } catch (error) {
        console.error('Failed to fetch rooms:', error);
        alert('Something went wrong. Please try again.');
    }


}

function renderRooms(rooms) {
    const grid = document.getElementById("roomsGrid");
    if (!rooms || rooms.length === 0) {
        grid.innerHTML = `
            <div class="alert alert-dark">
                No rooms available.
            </div>
        `;
        return;
    }
    grid.innerHTML = rooms.map(room => `
        <div class="card room-card">
            <div class="row g-0 align-items-center">
                <div class="col-md-3">
                    <img
                        src="images/Black_Cat_Hotel_Room.png"
                        class="img-fluid rounded-start room-image"
                        alt="Room">
                </div>
                <div class="col-md-7">
                    <div class="card-body py-2">
                        <p class="card-text mb-1">
                            <strong>${room.roomType}</strong>
                        </p>
                        <p class="card-text mb-1">
                            Max guests: ${room.maxGuests}
                        </p>
                        <p class="card-text mb-1">
                            Room numbers:
                            ${room.roomNumber}
                        </p>                        
                        <p class="card-text mb-1">
                            Extra bed: ${room.extraBedAvailable ? 'Available' : 'Not available'}
                        </p>
                        <p class="card-text mb-1">
                            Rating: <span id="rating-room-${room.id}">Loading...</span>
                        </p>

                    </div>
                </div>
                <div class="col-md-2 text-center">
                    <h5 class="mb-3">
                        ${room.roomPrice} kr
                    </h5>
                    <button class="btn btn-dark btn-sm" id="bookBtn" onclick="createReservation(${room.id})">
                        Book
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

function loadRoomRatings(rooms) {
    rooms.forEach(async (room) => {
        const ratingElement = document.getElementById(`rating-room-${room.id}`);
        if (!ratingElement) return;

        try {
            const token = localStorage.getItem('jwt');
            const headers = {};

            if (token) {
                headers["Authorization"] = `Bearer ${token}`;
            }

            const response = await fetch(`/reviews/room/avgRating/${room.roomNumber}`, {
                method: "GET",
                headers: headers
            });

            if (!response.ok || response.status === 204) {
                ratingElement.textContent = 'N/A';
                return;
            }

            const text = await response.text();

            if (!text || text.trim() === "") {
                ratingElement.textContent = 'N/A';
                return;
            }

            // Convert string to float, then round to the nearest whole integer
            const rawRating = parseFloat(text);

            if (!isNaN(rawRating) && rawRating > 0) {
                const roundedRating = Math.round(rawRating); // Converts e.g. 4.6 -> 5 or 4.2 -> 4
                ratingElement.textContent = `${roundedRating} / 5`;
            } else {
                ratingElement.textContent = 'N/A';
            }

        } catch (error) {
            console.error(`Failed to fetch rating for room ${room.roomNumber}:`, error);
            ratingElement.textContent = 'N/A';
        }
    });
}

async function createReservation(roomId) {
    const guests = document.getElementById("guests").value;
    const reservation = {
        roomId: roomId,
        checkIn: document.getElementById("checkIn").value,
        checkOut: document.getElementById("checkOut").value,
        guests: guests,
    };
    const token = localStorage.getItem("jwt");
    const response = await fetch('api/reservation', {
        method: "POST",
        headers: {
            "Content-type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(reservation)
    })

    if (!response.ok) {
        if (response.status === 403) {
            alert("You need to be logged in to make a reservation.");
            window.location.replace("/login");
            return;
        }
        if (response.status === 503) {
            alert("Customer-server is temporary unavailable. Try again later.");
            return;
        }

        const errorMessage = await response.text();
        alert(errorMessage);
        return;
    }

    const data = await response.json();
    showConfirmation(data);
}

function showConfirmation(reservation) {
    const container = document.getElementById("confirmationMessage")
    container.innerHTML = `
        <div class="alert mt-4">
            <h4>Booking Confirmed</h4>
            <p> Confirmation number: ${reservation.id} </p>
            <p> Room: ${reservation.room.roomType} </p>
            <p> Check-in: ${reservation.checkIn} </p>
            <p> Check-out: ${reservation.checkOut} </p>    
        </div>
    `;

    // Show toast
    const toast = document.getElementById('confirmationToast')
    bootstrap.Toast.getOrCreateInstance(toast).show();
}
