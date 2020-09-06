<template>
	<div class="container">
		<div id="personalData">
			<h1>Personal data</h1>
			<table id="tabela">
				<tr>
					<td>Username:</td>
					<td>{{guest.username}}</td>
				</tr>
				<tr>
					<td><floating-label placeholder="Name" type="text" id="name" :value="guest.name" :inputdata.sync="guest.name"/></td>
					<td id="emptyName">Enter your name</td>
				</tr>
				<tr>
					<td>Surname:</td>
					<td><input type="text" id="surname" v-model="guest.surname"></td>
					<td id="emptySurname">Enter your surname</td>
				</tr>
				<tr>
					<td>Sex:</td>
					<td><select name="sex" v-model="guest.sex">
							<option value="MALE">Male</option>
							<option value="FEMALE">Female</option>
							<option value="OTHER">Other</option>
							<option value="PRIVATE">Prefer not to say</option>
					</select></td>
				</tr>
				<tr>
					<td>Password:</td>
					<td><input type="password" id="password"
						v-model="guest.password"></td>
					<td id="emptyPassword">Enter your password</td>
				</tr>
				<tr>
					<td>Confirm password:</td>
					<td><input type="text" id="name" v-model="guest.name"></td>
					<td id="emptyName">Enter your password</td>
				</tr>
				<tr>
					<td><button v-on:click="updatePersonalData(guest)">Save
							changes</button></td>
					<td><router-link to="/home">Back</router-link></td>
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
            return{
                guest: { name: ""},
                successMsg: ""
            }
        },
        mounted(){
	    	var jwt = localStorage.jwt;
	    	
	    	if(jwt)
			{
		    	axios.get("rest/get_user",{headers:{"Authorization": "Bearer " + localStorage.jwt}})
			        .then(response => (this.guest = response.data))
		    		.catch(response => 
	    			{
	    				//TODO(Kristian): handle 404
	    				alert("Please log in");
	    			});
			}
    	},
    	
	    methods:
	    {
	        updatePersonalData: function(guest)
	        {
		        axios.post("rest/personal_data", guest)
		        .then(response =>
		            {
		                this.successMsg = "Changes successfully saved.";
		            })
		        .catch(response => {
		        		this.successMsg = "Something's wrong :(";
		        });
	        }
	    }
	}
</script>

<style scoped>
	table, tr, td {
		border: none;
	}
	
	#emptyName, #emptySurname, #emptyPassword {
		color: red;
	}
</style>
