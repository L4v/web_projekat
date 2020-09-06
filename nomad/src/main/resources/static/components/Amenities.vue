<template>
	<div class="container">
		<div id="amenities">
			<h1>Amenity</h1>
			<table>
				<td>
					<small class="error">{{errorMsg}}</small>
					<floating-label :inputdata.sync="amenityName" placeholder="New amenity name" name="amenityName" type="text"></floating label>
				</td>
				<td><button @click="addAmenity()">Add</button>
				<tr>
					<th>ID</th>
					<th>Name</th>
					<th>Modificate</th>
					<th>Remove</th>
				</tr>
				 <tr v-for="amenity in amenities">
					<td>{{amenity.id}}</td>
					<td v-if="!updating">{{amenity.name}}</td>
					<td v-else><floating-label :inputdata.sync="amenity.name" placeholder="Amenity name" name="amenityName" type="text"></floating label></td>
					<td><button @click="updateAmenity(amenity)">Modificate</button>
					<td><button @click="removeAmenity(amenity)">Remove</button>
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
				amenities: {},
				successMsg: "",
				errorMsg: "",
				updating: false,
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
	     	
	     	isEmpty()
	     	{
	     		errorMsg = "";
	     		if(!this.amenityName)
	     		{
	     			this.errorMsg = "Amenity name must not be empty";
	     			return false;
	     		}
	     		return true;
	     	},
	     	   
	     	addAmenity: function()
	     	{
	     		if(!this.isEmpty())
	     		{
	     			return;
	     		}
		
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
	     	
	     	updateAmenity: function(amenity)
	     	{
	     		this.updating = true;
	     	
	     		axios.post("rest/update_amenity", amenity)
		        .then(response =>
		            {
		                this.successMsg = "Amenity successfully updated.";
		            })
		        .catch(response => {
		        		this.successMsg = "Failed adding amenity";
		        });
		        
		        this.updating = false;
	     	}   
	    }
	}
</script>

<style scoped>
.error
    {
        color: #f00;
        padding: 0;
        padding-bottom: 8px;
        margin: 0;
        font-weight: 500;
    }
</style>