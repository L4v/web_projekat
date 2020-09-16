<template>
    <div id="reservations">
        <h1>Reservations</h1>
        <table>
            <tr>
                <th>Apartment</th>
                <th>Reserved date</th>
                <th>No. days</th>
                <th>Guest</th>
                <th>Status</th>
                <th></th>
                <th></th>
            </tr>
            <tr v-for="reservation in reservations">
                <td>{{reservation.apartment.location.address.street}} {{reservation.apartment.location.address.streetNumber}}  {{reservation.apartment.location.address.area}}</td>
                <td>{{reservation.reservation.startDate}}</td>
                <td>{{reservation.reservation.noDays}}</td>
                <td>{{reservation.guest.username}}</td>
                <td>{{reservation.reservation.status}}</td>
                <td 
                    v-if="reservation.reservation.status === 'CREATED'">
                    <button type="button" @click="acceptReservation(reservation.reservation.id)" class="button-primary">Accept</button>
                </td>
                <td 
                    v-else-if="reservation.reservation.status === 'ACCEPTED' && isFinishedReservation(reservation.reservation.id)">
                    <button type="button" @click="finishReservation(reservation.reservation)" class="button-primary">Finish</button>
                </td>
                <td 
                    v-if="reservation.reservation.status === 'CREATED' || reservation.reservation.status === 'ACCEPTED'">
                    <button type="button" @click="rejectReservation(reservation.reservation.id)">Reject</button>
                </td>
                <td v-else></td>
            </tr>
        </table>
    </div>
</template>

<script>
    module.exports =
    {
        data: function()
        {
            return {
                reservations: [],
            }
        },

        methods:
        {
            acceptReservation: function(reservationId)
            {
                let jwt = localStorage.jwt;
                if(!jwt)
                {
                    this.$router.go();
                    return;
                }

                axios.post("rest/host_accept_reservation", reservationId, {headers: {"Authorization": "Bearer " + jwt}})
                    .then(response => 
                    {
                        console.log(response.data);
                        this.$router.go();
                    })
                    .catch(response =>
                    {
                        console.log(response.data);
                    });
            },

            rejectReservation: function(reservationId)
            {
                let jwt = localStorage.jwt;
                if(!jwt)
                {
                    this.$router.go();
                    return;
                }

                axios.post("rest/host_reject_reservation", reservationId, {headers: {"Authorization": "Bearer " + jwt}})
                    .then(response =>
                    {
                        console.log(response.data);
                        this.$router.go();
                    })
                    .catch(response =>
                    {
                        console.log(response.data);
                    });
            },

            finishReservation: function()
            {
                let jwt = localStorage.jwt;
                if(!jwt)
                {
                    this.$router.go();
                    return;
                }

                
            },

            getReservationsPromise: function()
            {
                let jwt = localStorage.jwt;
                if(!jwt)
                {
                    this.$router.go();
                    return;
                }
                return axios.get("rest/host_all_reservations", {headers: {"Authorization": "Bearer " + jwt}})
                    .then(response => response.data);
            },

            isFinishedReservation: function(reservation)
            {
                if(!reservation)
                {
                    return false;
                }
                let currentDate = new Date();
                let reservationEndDate = new Date(reservation.startDate);
                reservationEndDate.setDate(reservationEndDate.getDate() + parseInt(reservation.noDays));
                if(currentDate.getTime() >= reservationEndDate.getTime())
                {
                    return true;
                }
                return false;
            },
        },
        mounted()
        {
            this.getReservationsPromise()
                .then(response =>
                {
                    let guestIds = [];
                    let apartmentIds = [];
                    response.forEach(reservation =>
                    {
                        this.reservations.push(
                            {
                                reservation: reservation,
                                guest:       {},
                                apartment:   {},
                            }
                        );
                        guestIds.push(reservation.guestId);
                        apartmentIds.push(reservation.apartmentId);
                    });

                    axios.get("rest/get_guests",
                    {
                        headers:
                        {
                            "Authorization": "Bearer " + localStorage.jwt,
                        },
                    }).then(response =>
                    {
                        response.data.forEach(guest =>
                        {
                            for(let reservation of this.reservations)
                            {
                                if(reservation.reservation.guestId === guest.username)
                                {
                                    reservation.guest = guest;
                                }
                            }
                        });
                    });

                    axios.get("rest/get_apartments",
                    {
                        headers:
                        {
                            "Authorization": "Bearer " + localStorage.jwt,
                        },
                    }).then(response =>
                    {
                        response.data.forEach(apartment =>
                        {
                            for(let reservation of this.reservations)
                            {
                                if(reservation.reservation.apartmentId === apartment.id)
                                {
                                    reservation.apartment = apartment;
                                }
                            }
                        });
                    });
                });
        }, 
    }
</script>