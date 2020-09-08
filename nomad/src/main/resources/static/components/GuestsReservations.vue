<template>
	<div class="container">
		<div id="reservations">
			<h1>Reservations</h1>
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
	</div>
</template>

<script>
	module.exports = {
		data: function()
		{
			return {
				reservations: {},
				successMsg: "",
			}
		},
		
		mounted()
		{
	    	var jwt = localStorage.jwt;

	    	if(jwt)
			{
		    	axios.get("rest/guest_view_reservations",{headers:{"Authorization": "Bearer " + localStorage.jwt}})
			        .then(response => (this.reservations = response.data))
		    		.catch(response => 
	    			{
	    				//TODO(Kristian): handle 404
	    				alert("Please log in");
	    			});
			}
	    },
	    
	    methods:
	    {
	     	cancelReservation: function(reservation)
	     	{
	     		axios.post("rest/guest_cancel_reservation", reservation)
		        .then(response =>
		            {
		                this.successMsg = "Reservation successfully cancelled.";
		            })
		        .catch(response => {
		        		this.successMsg = "Failed canceling reservation";
		        });
	     	}
	    }
	}

</script>