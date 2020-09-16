<template>
	<div class="container">
		<div id="apartments">
			<h1>Apartments</h1>
			<br>
			<div id="sorting">
				Sort by price:
				<select name="sort" v-model="sort" required>
	            	<option value="" disabled>Sort</option>
	            	<option value="ASCENDING">Ascending</option>
	            	<option value="DESCENDING">Descending</option>
	       		</select>
	       		<button class="button-primary" @click=sortApartmentsByPrice()>Sort</button>
	       	</div>
	       	<div id="filter">
	       		Filter:
	       		<select name="typeFilter" v-model="typeFilter" required>
	            	<option value="" disabled>Type</option>
	            	<option value="WHOLE">WHOLE</option>
	            	<option value="ROOM">ROOM</option>
	       		</select>
	       		<select name="statusFilter" v-model="statusFilter" required>
	            	<option value="" disabled>Status</option>
	            	<option value="ACTIVE">ACTIVE</option>
	            	<option value="INACTIVE">INACTIVE</option>
	       		</select>
	       		<button class="button-primary" @click=filterApartments()>Filter</button>
	       	</div>
	       	<br>
			<table>
				<tr>
					<th>ID</th>
					<th>Address</th>
					<th>Type</th>
					<th>Price</th>
					<th>Status</th>
					<th>Host username</th>
				</tr>
				 <tr v-for="apartment in apartments">
				 	<td>{{apartment.id}}</td>
				 	<td>{{apartment.location.address.street}} {{apartment.location.address.streetNumber}} {{apartment.location.address.area}}</td>
					<td>{{apartment.type}}</td>
					<td>{{apartment.price}}</td>
					<td>{{apartment.status}}</td>
					<td>{{apartment.hostId}}</td>
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
				apartments_copy: {},
				successMsg: "",
				sort: "",
				typeFilter: "",
				statusFilter: "",
			}
		},
		
		mounted()
		{
	    	let jwt = localStorage.jwt;
	    	
	    	
	    	if(jwt)
			{
		    	axios.get("rest/admin_all_apartments", {headers:{"Authorization": "Bearer " + jwt}})
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
			removeAll: function()
	     	{
				let jwt = localStorage.jwt;
				if(!jwt)
				{
					this.$router.go();
					return;
				}

	     		axios.post("rest/admin_remove_apartments", {}, {headers:{"Authorization": "Bearer " + jwt}})
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
	    			this.apartments.sort((a, b) => (a.price > b.price) ? 1 : -1);
	    		}
	    		else
	    		{
	    			this.apartments.sort((a, b) => (a.price < b.price) ? 1 : -1);
	    		}	
	     	},
	     	
	     	filterApartments: function()
	     	{
	     		if(!this.typeFilter && !this.statusFilter)
	     		{
	     			this.apartments = this.apartments_copy;
	     		} 
	     		else if(!this.typeFilter)
	     		{
	     			var retVal = [];
	     			for(apartment of this.apartments_copy)
	     			{
	     				if(apartment.status == this.statusFilter)
	     				{
	     					retVal.push(apartment);
	     				}
	     			}
	     			this.apartments = retVal;
	     		}
	     		else if(!this.statusFilter)
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
	     		else 
	     		{
	     			var retVal = [];
	     			for(apartment of this.apartments_copy)
	     			{
	     				if(apartment.status == this.statusFilter && apartment.type == this.typeFilter)
	     				{
	     					retVal.push(apartment);
	     				}
	     			}
	     			this.apartments = retVal;
	     		}
	     	},   
	    },
    }
</script>