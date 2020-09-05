<template>
	<div class="container">
		<div id="amenities">
			<h1>Amenity</h1>
			<table>
				<tr>
					<th>ID</th>
					<th>Name</th>
				</tr>
				 <tr v-for="amenity in amenities">
					<td>{{amenity.id}}</td>
					<td>{{amenity.name}}</td>
					<td><button v-on:click="removeAmenity(amenity)">Remove</button>
					<td><button v-on:click="updateAmenity(amenity)">Modificate</button>
				</tr>
				<tr>
					{{successMsg}}
				</tr>
				<tr>
					<td><floating-label placeholder="Amenity name" type="text" v-model="amenityName"></td>
					<td><button v-on:click="addAmenity()">Add</button>
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
				amenities: {},
				successMsg: "",
			}
		},
		
		mounted()
		{
	    	var jwt = localStorage.jwt;
	    	
	    	if(jwt)
			{
		    	axios.get("rest/admin_all_amenities",{headers:{"Authorization": "Bearer " + localStorage.jwt}})
			        .then(response => (this.amenities = response.data))
		    		.catch(response => 
	    			{
	    				//TODO(Kristian): handle 404
	    				alert("Please log in");
	    			});
			}
	    },
	    
	    methods:
	    {
	     	removeAmenity: function(amenity)
	     	{
	     		axios.post("rest/remove_amenity", amenity)
		        .then(response =>
		            {
		                this.successMsg = "Amenity successfully removed.";
		            })
		        .catch(response => {
		        		this.successMsg = "Failed removing amenity";
		        });
	     	},   
	     	addAmenity: function()
	     	{
	     		let amenity = 
	     		{
	     			id: Math.random(),
	     			name: this.amenityName,
	     		};
	     		
	     		axios.post("rest/add_amenity", amenity)
		        .then(response =>
		            {
		                this.successMsg = "Amenity successfully added.";
		            })
		        .catch(response => {
		        		this.successMsg = "Failed adding amenity";
		        });
	     	},
	     	
	     	updateAmenity: function()
	     	{
	     		axios.post("rest/update_amenity", amenity)
		        .then(response =>
		            {
		                this.successMsg = "Amenity successfully updated.";
		            })
		        .catch(response => {
		        		this.successMsg = "Failed adding amenity";
		        });
	     	}   
	    }
	}
</script>
