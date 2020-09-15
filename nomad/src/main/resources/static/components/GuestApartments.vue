<template>
	<div class="container">
		<div id="apartments">
			<h1>Apartments</h1>
			<div id="sort">
				Sort by price:
				<select name="sort" v-model="sort" required>
	            	<option value="" disabled>Sort</option>
	            	<option value="ASCENDING">Ascending</option>
	            	<option value="DESCENDING">Descending</option>
	       		</select>
	       		<button class="button-primary" @click=sortApartmentsByPrice()>Sort</button>
       		</div>
			<table>
				<tr>
					<th>Type</th>
					<th>Status</th>
					<th>Price</th>
					<th>Comment</th>
					<th>Rating</th>
					<th>Submit</th>
				</tr>
				 <tr v-for="apartment in apartments">
					<td>{{apartment.type}}</td>
					<td>{{apartment.status}}</td>
					<td>{{apartment.price}}</td>
					<td><router-link :to="{ name: 'ShowApartment', params: { apartment } }">More</router-link></td>
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
				apartments: {},
				successMsg: "",
				sort: "",
			}
		},
		
		mounted()
		{
	    	var jwt = localStorage.jwt;
	    	
	    	if(jwt)
			{
		    	axios.get("rest/guest_reserved_apartments", {headers:{"Authorization": "Bearer " + localStorage.jwt}})
			        .then(response => 
			       	{
			       		this.apartments = response.data;
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
	    
	    	sortApartmentsByPrice: function()
	    	{
	    		if(!this.sort)
	    		{
	    			this.successMsg = "Sort parameter must not be empty.";
	    		} 
	    		else if(this.sort == 'DESCENDING')
	    		{
	    			this.reservations.sort((a, b) => (a.price > b.price) ? 1 : -1);
	    		}
	    		else
	    		{
	    			this.reservations.sort((a, b) => (a.price < b.price) ? 1 : -1);
	    		}	
	    	},
	    },
    }
</script>