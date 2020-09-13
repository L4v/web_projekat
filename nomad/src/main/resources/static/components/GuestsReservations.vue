<template>
	<div class="container">
		<div id="reservations">
			<h1>Reservations</h1>
			<div id="sorting">
				Sort by price:
				<select name="sort" v-model="sort" required>
	            	<option value="" disabled>Sort</option>
	            	<option value="ASCENDING">Ascending</option>
	            	<option value="DESCENDING">Descending</option>
	       		</select>
	       		<button class="button-primary" @click="sortReservations()">Sort</button>
	       	</div>
			<table>
				<tr>
					<th>Start date</th>
					<th>Number of days</th>
					<th>Total price</th>
					<th>Status</th>
					<th>Cancel reservation</th>
				</tr>
				 <tr v-for="reservation in reservations">
					<td>{{reservation.startDate}}</td>
					<td>{{reservation.noDays}}</td>
					<td>{{reservation.totalPrice}}</td>
					<td>{{reservation.status}}</td>
					<td v-if="reservation.status === 'ACCEPTED' || reservation.status === 'CREATED'"><button class="button-primary" @click="cancelReservation(reservation)">Cancel</button>
					<td v-else><button disabled>Cancel</button>
				</tr>
				<tr>
					{{successMsg}}
				</tr>
			</table>
		</div>
		<button v-if="!showForm" type="button" @click="showForm = !showForm">+</button>
		<div v-if="showForm" id="add-reservation-form">
			<select name="apartment" v-model="selectedApartment" required>
				<option value="" disabled>Select apartment</option>
				<option v-for="apartment in apartments" :value="apartment">{{apartment.location.address.street}} {{apartment.location.address.streetNumber}} {{apartment.location.address.area}}</option>
			</select>
			<div v-if="selectedApartment" id="apartment-info">
				<h2>{{selectedApartment.location.address.street}}</h2>
			</div>
		</div>
	</div>
</template>

<script>
	module.exports = {
		data: function()
		{
			return {
				reservations: {},
				sort: "",
				successMsg: "",
				showForm: false,
				apartments: [],
				selectedApartment: null,
			}
		},
		
		mounted()
		{
			var jwt = localStorage.jwt;
			if(!jwt)
			{
				alert("Please log in");
				return;
			}
			if(jwt)
			{
				axios.get("rest/guest_view_reservations",{headers:{"Authorization": "Bearer " + localStorage.jwt}})
					.then(response => (this.reservations = response.data))
					.catch(response => 
					{
						//TODO(Kristian): handle 404
					});
			}
			this.getApartments();
		},
		
		methods:
		{
			// NOTE(Jovan): Gets all available apartments
			getApartments: function()
			{
				console.log("Getting apartments");
				let jwt = localStorage.jwt;
				if(!jwt)
				{
					// TODO(Jovan): Handle?
					console.log("No jwt");
					return;
				}
				axios.get("rest/guest_all_apartments", {headers: {"Authorization": "Bearer " + jwt}})
					.then(response =>
					{
						this.apartments = response.data;
					})
					.catch(response =>
					{
						// TODO(Jovan): Handle;
					});
			},

			sortReservations: function()
			{
				if(!this.sort)
				{
					this.successMsg = "Sort parameter must not be empty.";
				} 
				else if(this.sort == 'DESCENDING')
				{
					this.reservations.sort((a, b) => (a.totalPrice > b.totalPrice) ? 1 : -1);
				}
				else
				{
					this.reservations.sort((a, b) => (a.totalPrice < b.totalPrice) ? 1 : -1);
				}	    		
			},
		
			cancelReservation: function(reservation)
			{
				axios.post("rest/guest_cancel_reservation", reservation, {headers:{"Authorization": "Bearer " + localStorage.jwt}})
				.then(response =>
					{
						this.reservations = response.data;
						this.successMsg = "Reservation successfully cancelled.";
					})
				.catch(response => {
						this.successMsg = "Failed canceling reservation";
				});
			}
		}
}

</script>