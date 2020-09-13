<template>
	<div class="container">
		<div id="apartments">
			<h1>Apartments</h1>
			<div id="sorting">
				Sort by price:
				<select name="sort" v-model="sort" required>
	            	<option value="" disabled>Sort</option>
	            	<option value="ASCENDING">Ascending</option>
	            	<option value="DESCENDING">Descending</option>
	       		</select>
	       		<button class="button-primary" @click=sortApartmentsByPrice()>Sort</button>
	       	</div>
			<table>
				 <tr v-for="apartment in apartments">
					<td>{{apartment.type}}</td>
					<td>{{apartment.status}}</td>
					<td>{{apartment.price}}</td>
				</tr>
			</table>
			<button @click="removeAll()">Remove all</button>
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
		    	axios.get("rest/admin_all_apartments", {headers:{"Authorization": "Bearer " + localStorage.jwt}})
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
			removeAll: function()
	     	{
	     		axios.post("rest/admin_remove_apartments",{headers:{"Authorization": "Bearer " + localStorage.jwt}})
		        .then(response =>
	            {
	            	this.apartments = response.data;
	                this.successMsg = "Apartments successfully removed.";
	            })
		        .catch(response => {
		        		this.successMsg = "Failed removing apartments";
		        });
	     	},
	     	
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