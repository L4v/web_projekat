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
	            	<option value="ALL">ALL</option>
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
			{{successMsg}}
		</div>
	</div>
</template>

<script>
	module.exports = {
		data: function()
		{
			return {
				reservations: [],
				reservations_copy: [],
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
			        	this.reservations_copy = response.data;
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
	    	
	    	filterReservations: function()
	    	{
	    		if(!this.filter)
	    		{
	    			this.successMsg = "Filter parameter must not be empty.";
	    		} 
	    		else if(this.filter == 'ALL')
	    		{
	    			this.reservations = this.reservations_copy;
	    		}
	    		else
	    		{
		    		var retVal = [];
		    		for(reservation of this.reservations_copy)
		    		{
		    			if(reservation.status == this.filter)
		    			{
		    				retVal.push(reservation);
		    			}
		    		}
		    		this.reservations = retVal;
	    		}
	    	},
	    },
	}
</script>