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
					<td>
						<small class="error">{{errors.name}}</small>
						<floating-label :inputdata.sync="guest.name" placeholder="Name" type="text"></floating-label>
					</td>
				</tr>
				<tr>
					<td>
						<small class="error">{{errors.surname}}</small>
						<floating-label :inputdata.sync="guest.surname" placeholder="Surname" type="text"></floating-label>
					</td>
				</tr>
				<tr>
					<td>Sex</td>
					<td><select name="sex" v-model="guest.sex">
							<option value="MALE">Male</option>
							<option value="FEMALE">Female</option>
							<option value="OTHER">Other</option>
							<option value="PRIVATE">Prefer not to say</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						<small class="error">{{errors.password}}</small>
						<floating-label :inputdata.sync="guest.password" placeholder="Password" type="password"></floating-label>
					</td>
				</tr>
				<tr>
					<td>
						<small class="error">{{errors.confirmPass}}</small>
						<floating-label :inputdata.sync="guest.password" placeholder="Confirm password" name="confirmPassword" type="password"></floating-label>
					</td>
				</tr>
				<tr>
					<td><button @click="updatePersonalData(guest)">Save changes</button></td>
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
                guest: {},
                successMsg: "",
                errors: {name: "", surname: "", password: "", confirmPass: ""},
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
			validateNameAndSurname: function()
			{
				if(!this.guest.name)
                {
                    this.errors.name = "Name must not be empty";
                    return false;
                }
                if(!this.guest.surname)
                {
                    this.errors.surname = "Surname must not be empty";
                    return false;
                }
                let regex = /^[A-Za-z]+/;
                if(!regex.test(this.guest.name))
                {
                    this.errors.name = "Name must contain alphabet letters only";
                    return false;
                }
                if(!regex.test(this.guest.surname))
                {
                    this.errors.surname = "Surname must contain alphabet letters only";
                    return false;
                }
                return true;
			},
            validatePassword: function()
            {
                if(!this.guest.password)
                {
                    this.errors.password = "Password must not be empty";
                    return false;
                }
                if(this.guest.password.length < 8)
                {
                    this.errors.password = "Password must be at least 8 characters long";
                    return false;
                }
                let regex = /^[A-Za-z0-9!@#\$%\^&\*]+/;
                if(!regex.test(this.guest.password))
                {
                    this.errors.password = "Password can contain alphabet letters, numbers and !@#$%^&* only";
                    return false;
                }
                //TODO: resiti proveru za ispravnost pass i confirm
                if(!personalData.getElementsByName("confirmPassword"))
                {
                    this.errors.confirmPass = "Password must not be empty";
                    return false;
                }
                if(!personalData.getElementsByName("confirmPassword").equals(this.guest.password))
                {
                	this.errors.confirmPass = "Not same password";
                	return false;
                }
                return true;
            },
	    	validateFields: function()
	    	{
	    		this.errors.name = "";
	        	this.errors.surname = "";
	        	this.errors.password = "";
	        	this.errors.confirmPassword = "";
	        	return this.validateNameAndSurname() && this.validatePassword();
	    	},
	        updatePersonalData: function(guest)
	        {        
				/*if(!this.validateFields())
                {
                    return;
                }*/
	        
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
	
	.error
    {
        color: #f00;
        padding: 0;
        padding-bottom: 8px;
        margin: 0;
        font-weight: 500;
    }
</style>
