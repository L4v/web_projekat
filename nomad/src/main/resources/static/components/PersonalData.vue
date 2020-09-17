<template>
	<div class="container">
		<div id="personalData">
			<h1>Personal data</h1>
			<br>
			<table id="tabela">
				<tr>
					<floating-label readonly :inputdata.sync="guest.username" placeholder="Username" type="text" class="no-focus" disabled="disabled" readonly="readonly" tabindex="-1"></floating-label>
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
					<td>
						<select id="sex" v-model="guest.sex" required>
		                    <option value="" disabled>Sex</option>
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
						<floating-label :inputdata.sync="confirmPassword" placeholder="Confirm password" type="password"></floating-label>
					</td>
				</tr>
				<tr>
					<td><button id="saveButton" class="button-primary" @click="updatePersonalData(guest)">Save changes</button></td>
				</tr>
			</table>
			{{successMsg}}
		</div>
	</div>
</template>

<script>
	module.exports = {
		data: function()
        {
            return{
                guest: {},
                confirmPassword: "",
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
                if(!this.confirmPassword)
                {
                    this.errors.confirmPass = "Password must not be empty";
                    return false;
                }
                if(!(this.confirmPassword == this.guest.password))
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
				if(!this.validateFields())
                {
                    return;
                }
	        
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
    .no-focus 
    {
    	pointer-events: none;
    	display: block;
    	-webkit-user-select: none;
  		-moz-user-select: none;
 		-ms-user-select: none;
  		user-select: none; 
 		user-focus: none;
 		user-modify: read-only;
	}
	#sex
    {
    	display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }

</style>
