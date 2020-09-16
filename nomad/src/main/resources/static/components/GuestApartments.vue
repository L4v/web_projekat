<template>
	<div class="container">
		<div id="apartments">
			<h1>Apartments</h1>
			<br>
			<div id="sort">
				Sort by price:
				<select name="sort" v-model="sort" required>
	            	<option value="" disabled>Sort</option>
	            	<option value="ASCENDING">Ascending</option>
	            	<option value="DESCENDING">Descending</option>
	       		</select>
	       		<button class="button-primary" @click=sortApartmentsByPrice()>Sort</button>
       		</div>
       		<div id="search">
       			Filter:
       			<select name="typeFilter" v-model="typeFilter" required>
	            	<option value="" disabled>Type</option>
	            	<option value="WHOLE">WHOLE</option>
	            	<option value="ROOM">ROOM</option>
	       		</select>
	       		<button class="button-primary" @click=filterApartments()>Filter</button>
       		</div>
       		<br>
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
				apartments_copy: {},
				successMsg: "",
				sort: "",
				typeFilter: "",
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
			       		this.apartments_copy = response.data;
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
	    			this.apartments.sort((a, b) => (a.price > b.price) ? 1 : -1);
	    		}
	    		else
	    		{
	    			this.apartments.sort((a, b) => (a.price < b.price) ? 1 : -1);
	    		}	
	    	},
	    	
	    	filterApartments: function()
	     	{
	     		if(!this.typeFilter)
	     		{
	     			this.apartments = this.apartments_copy;
	     		} 
	     		else 
	     		{
	     			var retVal = [];
	     			for(apartment of this.apartments_copy)
	     			{
	     				if(apartment.type == this.typeFilter)
	     				{
	     					retVal.push(apartment);
	     				}
	     			}
	     			this.apartments = retVal;
	     		}
	     		//TODO
	     	},   
	    	
	    },
    }
</script>