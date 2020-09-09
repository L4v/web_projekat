<template>
	<div class="container">
		<div id="showGuests">
			<h1>Guests</h1>
			<div id="search">
				<floating-label :inputdata.sync="guest_username" placeholder="Type username" type="text"></floating-label>
				<select id="sex" v-model="guest_sex" required>
		            <option value="" disabled>Sex</option>
		            <option value="MALE">Male</option>
		            <option value="FEMALE">Female</option>
		            <option value="OTHER">Other</option>
		            <option value="PRIVATE">Prefer not to say</option>
		        </select>
				<button class="button-primary" @click="searchGuest()">Search</button>
			</div>
			<table id="MyGuests">
				<tr>
					<th>Username</th>
					<th>Name</th>
					<th>Surname</th>
					<th>Sex</th>
				</tr>
				<tr v-for="guest in guests">
					<td>{{guest.username}}</td>
					<td>{{guest.name}}</td>
					<td>{{guest.surname}}</td>
					<td>{{guest.sex}}</td>
				</tr>
			</table>
			{{message}}
		</div>
	</div>
</template>

<script>
	module.exports = {
		data: function()
		{
			return{
				guests: {},
				guest_username: "",
				guest_sex: "",
				message: "",
			}
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
	        
	            if(!this.guest_username && !this.guest_sex)
	            {
	            	alert("Search parameter must not be empty.");
	            	return;
	            }
	            
	            let parameter = 
	            {
	            	username: this.guest_username,
	            	sex: this.guest_sex,
	            };
	            
	            axios.post("rest/host_search_guests", parameter, {headers:{"Authorization": "Bearer " + localStorage.jwt}})
		             .then(response =>
		             {
		                 this.guests = response.data;
		             })
		             .catch(response =>
		             {
		                 this.message = "No results";
		             });

	        }
	    }
	}
</script>