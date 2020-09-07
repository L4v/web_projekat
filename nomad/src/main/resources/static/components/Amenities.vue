<template>
	<div class="container">
		<div id="amenities">
			<h1>Amenities</h1>
			<table>
				<td>
					<small class="error">{{errorMsg}}</small>
					<floating-label :inputdata.sync="amenityName" placeholder="New amenity name" type="text"></floating label>
				</td>
				<td><button class="button-primary" @click="addAmenity()">Add</button>
				<tr>
					<th>ID</th>
					<th>Name</th>
					<th>Modify</th>
					<th>Remove</th>
				</tr>
				 <tr v-for="amenity in amenities">
					<td>{{amenity.id}}</td>
					<td v-if="updating_id != amenity.id">{{amenity.name}}</td>
					<td v-else><input v-model="amenity.name" type="text"/></td>
					<td v-if="updating_id != amenity.id"><button class="button-primary" @click="modifyAmenity(amenity.id)">Modify</button>
					<td v-else><button class="button-primary" @click="updateAmenity(amenity)">Save</button>
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
				updating_id: "",
				amenityName: "",
			}
		},
		
		mounted()
		{
	    	var jwt = localStorage.jwt;
	    	
	    	if(jwt)
			{
		    	axios.get("rest/admin_all_amenities", {headers:{"Authorization": "Bearer " + localStorage.jwt}})
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
	     		axios.post("rest/remove_amenity", amenity, {headers:{"Authorization": "Bearer " + localStorage.jwt}})
		        .then(response =>
		            {
		            	this.amenities = response.data;
		                this.successMsg = "Amenity successfully removed.";
		            })
		        .catch(response => {
		        		this.successMsg = "Failed removing amenity";
		        });
	     	},
	     	
	     	modifyAmenity: function(amenity_id)
	     	{
	     		this.updating_id = amenity_id;
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
	     		
	     		axios.post("rest/add_amenity", amenity, {headers:{"Authorization": "Bearer " + localStorage.jwt}})
		        .then(response =>
		            {
		            	this.amenities = response.data;
		                this.successMsg = "Amenity successfully added.";
		            })
		        .catch(response => {
		        		this.successMsg = "Failed adding amenity";
		        });
	     	},
	     	
	     	updateAmenity: function(amenity)
	     	{
	     		axios.post("rest/update_amenity", amenity, {headers:{"Authorization": "Bearer " + localStorage.jwt}})
		        .then(response =>
		            {
		            	this.amenities = response.data;
		                this.successMsg = "Amenity successfully updated.";
		            })
		        .catch(response => {
		        		this.successMsg = "Failed adding amenity";
		        });
		        
		        this.updating_id = "";
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