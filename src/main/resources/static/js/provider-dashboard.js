let currentBookings = [];
const PROVIDER_ID = 1; // In production, this should come from your Auth session

document.addEventListener('DOMContentLoaded', () => {
    loadBookings();
});

function loadBookings() {
    fetch(`/api/bookings/provider/${PROVIDER_ID}`)
        .then(res => res.json())
        .then(responseObj => {
            if (responseObj.responseCode === "00000000") {
                currentBookings = responseObj.data || [];
                renderTable(currentBookings);
                document.getElementById('totalCount').innerText = currentBookings.length;
            }
        })
        .catch(err => console.error("Load failed:", err));
}

function renderTable(bookings) {
    const tableBody = document.getElementById('bookingTableBody');
    tableBody.innerHTML = bookings.map(booking => `
        <tr>
            <td class="ps-4 fw-semibold">${booking.customerName}</td>
            <td>${booking.categoryName}</td>
            <td>
                <div>${booking.appointmentDate}</div>
                <small class="text-muted">${booking.appointmentTime}</small>
            </td>
            <td><span class="badge status-badge ${getStatusClass(booking.status)}">${booking.status}</span></td>
            <td class="text-center">
                <button class="btn btn-sm btn-primary px-3" onclick="viewDetails(${booking.bookingId})">
                    View & Act
                </button>
            </td>
        </tr>
    `).join('');
}

function viewDetails(id) {
    const booking = currentBookings.find(b => b.bookingId === id);
    const modalDetails = document.getElementById('modalDetails');
    const modalFooter = document.getElementById('modalFooter');
    const myModal = new bootstrap.Modal(document.getElementById('bookingModal'));

    modalDetails.innerHTML = `
        <div class="mb-3">
            <label class="text-muted small">Customer Address</label>
            <p class="fw-bold"><i class="bi bi-geo-alt-fill text-danger"></i>
               ${booking.customerAddress || 'Indore, Madhya Pradesh'}</p>
        </div>
        <div class="row mb-3">
            <div class="col-6">
                <label class="text-muted small">Contact</label>
                <p class="mb-0"><i class="bi bi-telephone-fill"></i> ${booking.customerPhone || 'N/A'}</p>
            </div>
            <div class="col-6 text-end">
                <label class="text-muted small">Booking ID</label>
                <p class="mb-0">#${booking.bookingId}</p>
            </div>
        </div>
        <hr>
        <div class="p-3 bg-light rounded">
            <div class="d-flex justify-content-between">
                <span>Requested Time:</span>
                <span class="fw-bold text-primary">${booking.appointmentTime}</span>
            </div>
        </div>
    `;

    if (booking.status === 'PENDING') {
        modalFooter.innerHTML = `
            <button class="btn btn-outline-danger me-auto" onclick="updateStatus(${id}, 'CANCELLED')">Reject</button>
            <button class="btn btn-success px-4" onclick="updateStatus(${id}, 'CONFIRMED')">Accept Booking</button>
        `;
    } else if (booking.status === 'CONFIRMED') {
        modalFooter.innerHTML = `
            <button class="btn btn-info w-100 text-white" onclick="completeBooking(${id})">Mark as Completed</button>
        `;
    } else {
        modalFooter.innerHTML = `<button class="btn btn-secondary w-100" data-bs-dismiss="modal">Close</button>`;
    }

    myModal.show();
}

function updateStatus(id, status) {
    fetch(`/api/bookings/${id}/status?status=${status}`, { method: 'PUT' })
        .then(res => res.json())
        .then(data => {
            if (data.responseCode === "00000000") {
                alert(`Booking ${status.toLowerCase()} successfully!`);
                location.reload();
            }
        });
}

function completeBooking(id) {
    fetch(`/api/bookings/${id}/complete`, { method: 'PUT' })
        .then(res => res.json())
        .then(data => {
            if (data.responseCode === "00000000") {
                alert("Service Completed!");
                location.reload();
            }
        });
}

function getStatusClass(status) {
    switch(status) {
        case 'PENDING': return 'bg-warning text-dark';
        case 'CONFIRMED': return 'bg-primary';
        case 'COMPLETED': return 'bg-success';
        case 'CANCELLED': return 'bg-danger';
        default: return 'bg-secondary';
    }
}