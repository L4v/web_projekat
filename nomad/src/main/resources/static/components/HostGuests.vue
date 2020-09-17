<template>
	<div class="container">
		<div id="showGuests">
			<h1>Guests</h1>
			<br>
			<div id="search">
				Search:
				<select id="sex" v-model="guest_sex" required>
		            <option value="" disabled>Sex</option>
		            <option value="">All</option>
		            <option value="MALE">Male</option>
		            <option value="FEMALE">Female</option>
		            <option value="OTHER">Other</option>
		            <option value="PRIVATE">Prefer not to say</option>
		        </select>
		        <input v-model="guest_username" placeholder="Username..." type="text">
				<button class="button-primary" @click="searchGuest()">Search</button>
			</div>
			<br>
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
				guests_copy: {},
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
		                 this.guests_copy = response.data;
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
					this.guests = this.guests_copy;
	            }
	            else if(!this.guest_username)
	            {
			        var result = [];
			        for(guest of this.guests_copy)
			        {
			        	if(guest.sex == this.guest_sex)
			        	{
			        		result.push(guest);
			        	}
			        }
					this.guests = result;
				}
				else if(!this.guest_sex)
				{
					var result = [];
		    		for(let guest of this.guests_copy)
		    		{
		    			if(guest.username == this.guest_username)
		    			{
		    				result.push(guest);
		    			}
		    		}
		    		this.guests = result;
				}
				else
				{
					var result = [];
		    		for(let guest of this.guests_copy)
		    		{
		    			if(guest.username == this.guest_username && guest.sex == this.guest_sex)
		    			{
		    				result.push(guest);
		    			}
		    		}
		    		this.guests = result;
				}
	        },
	    },
	}
</script>