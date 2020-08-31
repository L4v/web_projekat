<template>
	<div class="container">
		<div id="amenities">
			<h1>Amenity</h1>
			<table id="tabela">
				<tr bgcolor="lightgrey">
					<th>ID</th>
					<th>Name</th>
				</tr>
				 <tr v-for="amenity in amenities">
					<td>{{amenity.id}}</td>
					<td>{{amenity.name}}</td>
					<td><button v-on:click="removeAmenity(amenity)">Delete</button>
				</tr>
					{{successMsg}}
				</tr>
			</table>
		</div>
	</div>
</template>

<script>
	module.exports = {
		data:function()
		{
			amenities: {},
        	successMsg: ""
		},
		
		mounted(){
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
	        
	    }
	}
</script>