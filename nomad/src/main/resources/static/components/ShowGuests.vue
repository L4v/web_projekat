<template>
	<div class="container">
		<div id="showGuests">
			<h1>Guests</h1>
			<floating-label :inputdata.sync="..." placeholder="Search parameter" type="text"></floating-label>
			<button @click="searchGuest()">Search</button>
			<table id="MyGuests">
				<tr>
					<th>Username</th>
					<th>Name</th>
					<th>Surname</th>
				</tr>
				<tr v-for="guest in guests">
					<td>{{guest.username}}</td>
					<td>{{guest.name}}</td>
					<td>{{guest.surname}}</td>
				</tr>
			</table>
		</div>
	</div>
</template>

<script>
	module.exports = {
		data: function()
		{
			guests: {}
		},
		
		mounted(){
	    	 var jwt = localStorage.jwt;
	    	 if(jwt)
	    	 {
	         	axios.get("rest/host_all_guests", {headers:{"Authorization": "Bearer " + jwt}})
		             .then(response =>
		             {
		                 this.guests = response.data;
		             })
		             .catch(response =>
		             {
		                 
		             });
		     }
	    },
    	
	    methods:
	    {
	        searchGuest: function()
	        {
	            
	        }
	    }
	}
</script>