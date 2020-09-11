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
	       		<button class="button-primary" @click=sortReservations()>Sort</button>
	       	</div>
	       	<div id="filter">
	       		Filter by status:
	       		<select name="filter" v-model="filter" required>
	            	<option value="" disabled>Filter</option>
	            	<option value="CREATED">CREATED</option>
	            	<option value="REJECTED">REJECTED</option>
	            	<option value="CANCELLED">CANCELLED</option>
	            	<option value="ACCEPTED">ACCEPTED</option>
	            	<option value="FINISHED">FINISHED</option>
	       		</select>
	       		<button class="button-primary" @click=filterReservations()>Filter</button>
	       	</div>
			<table>
				<tr>
					<th>Start date</th>
					<th>No of days</th>
					<th>Total price</th>
					<th>Status</th>
				</tr>
				 <tr v-for="reservation in reservations">
					<td>{{reservation.startDate}}</td>
					<td>{{reservation.noDays}}</td>
					<td>{{reservation.totalPrice}}</td>
					<td>{{reservation.status}}</td>
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
				reservations: [],
				sort: "",
				filter: "",
				successMsg: "",
			}
		},
		
		mounted()
		{
	    	var jwt = localStorage.jwt;
	    	if(jwt)
			{
		    	axios.get("rest/admin_view_reservations",{headers:{"Authorization": "Bearer " + localStorage.jwt}})
			        .then(response => 
			        {
			        	this.reservations = response.data;
			       	})
		    		.catch(response => 
	    			{
	    				//TODO(Kristian): handle 404
	    				alert("Please log in");
	    			});
			}
	    },
	    
	    methods:
	    {
	    	sortReservations: function()
	    	{
	    		if(!this.sort)
	    		{
	    			this.successMsg = "Sort parameter must not be empty.";
	    			return;
	    		}
	    	},
	    	
	    	filterReservations: function()
	    	{
	    		if(!this.filter)
	    		{
	    			this.successMsg = "Filter parameter must not be empty.";
	    			return;
	    		}
	    	},
	    },
	}
</script>