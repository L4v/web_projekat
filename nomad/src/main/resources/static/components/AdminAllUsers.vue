<template>
    <div class="container">
    	<h1>Users</h1>
    	<br>
			<div id="search">
				Search:
				<select id="sex" v-model="userSex" required>
		            <option value="" disabled>Sex</option>
		            <option value="">All</option>
		            <option value="MALE">Male</option>
		            <option value="FEMALE">Female</option>
		            <option value="OTHER">Other</option>
		            <option value="PRIVATE">Prefer not to say</option>
		        </select>
		        <select id="userType" v-model="userType" required>
		            <option value="" disabled>Type</option>
		            <option value="">All</option>
		            <option value="GUEST">Guest</option>
		            <option value="HOST">Host</option>
		            <option value="ADMIN">Admin</option>
		        </select>
		        <input v-model="userUsername" placeholder="Username..." type="text">
				<button class="button-primary" @click="searchUser()">Search</button>
			</div>
			<br>
        <table>
            <tr>
                <th>Username</th>
                <th>Name</th>
                <th>Surname</th>
                <th>User Type</th>
                <th>Sex</th>
            </tr>
            <tr v-for="user in users" :key="user">
                <td>{{user.username}}</td>
                <td>{{user.name}}</td>
                <td>{{user.surname}}</td>
                <td>{{user.userType}}</td>
                <td>{{user.sex}}</td>
            </tr>
        </table>
        <!-- TODO(Jovan): This is temporary, will add correct form later -->
        <button v-if="!showForm" @click="showForm = !showForm">+</button>
        <form v-if="showForm" @submit.prevent="registerHost" id="registration-form">
            <b class="success">{{successMsg}}</b>
            <small class="error">{{errors.username}}</small>
            <floating-label type="text" name="username"  placeholder="Username" :inputdata.sync="username"></floating-label>
            <small class="error">{{errors.password}}</small>
            <floating-label type="password" name="password"  placeholder="Password" :inputdata.sync="password"></floating-label>
            <small class="error">{{errors.confpassword}}</small>
            <floating-label type="password" name="confpassword"  placeholder="Confirm password" :inputdata.sync="confpassword"></floating-label>
            <small class="error">{{errors.name}}</small>
            <floating-label type="text" name="name"  placeholder="Name" :inputdata.sync="name"></floating-label>
            <small class="error">{{errors.surname}}</small>
            <floating-label type="text" name="surname"  placeholder="Surname" :inputdata.sync="surname"></floating-label>
            <small class="error">{{errors.sex}}</small>
            <select name="sex" v-model="sex" required>
                <option value="" disabled>Sex</option>
                <option value="MALE">Male</option>
                <option value="FEMALE">Female</option>
                <option value="OTHER">Other</option>
                <option value="PRIVATE">Prefer not to say</option>
            </select>
            <button class="button-primary" @click="registerHost">Add new host</button>
        </form>
    </div>
</template>

<script>
    module.exports =
    {
        data: function()
        {
            return {
                users: [],
                users_copy: [],
                userType:     "",
                userSex:      "",
                userUsername: "",
                username:     "",
                password:     "",
                confpassword: "",
                name:         "",
                surname:      "",
                sex:          "",
                errors: 
                    {
                        username:     "",
                        password:     "",
                        confpassword: "",
                        name:         "",
                        surname:      "",
                        sex:          "",
                    },
                successMsg: "",
                showForm: false,
            }
        },
        methods:
        {
            getUsers: function()
            {
                let jwt = localStorage.jwt;
                if(!jwt)
                {
                    // TODO(Jovan): Handle login redirect?
                    return;
                }
                axios.get("rest/admin_all_users", {headers: {"Authorization": "Bearer " + jwt}})
                    .then(response =>
                    {
                        this.users = response.data;
                        this.users_copy = response.data;
                    })
                    .catch(response =>
                    {
                        // TODO(Jovan): Handle failure
                    });
            },
            validateName: function()
            {
                if(!this.name)
                {
                    this.errors.name = "Name can't be empty";
                    return false;
                }
                let regex = /^[A-Z][a-z]*/;
                if(!regex.test(this.name))
                {
                    this.errors.name = "Name must be capital and can contain alphabet letters only";
                    return false;
                }
                return true;
            },
            validateSurname: function()
            {
                if(!this.surname)
                {
                    this.errors.surname = "Surname can't be empty";
                    return false;
                }
                let regex = /^[A-Z][a-z]*/;
                if(!regex.test(this.surname))
                {
                    this.errors.surname = "Surname must be capital and can contain alphabet letters only";
                    return false;
                }
                return true;
            },
            validateSex: function()
            {
                if(!this.sex)
                {
                    this.errors.sex = "Please select an option";
                    return false;
                }
                return true;
            },
            validateUsername: function()
            {
                if(!this.username)
                {
                    this.errors.username = "Username can't be empty";
                    return false;
                }
                let regex = /^[A-Za-z]+/;
                if(!regex.test(this.username))
                {
                    this.errors.username = "Username can contain alpohabet letters only";
                    return false;
                }
                return true;
            },
            validatePassword: function()
            {
                if(!this.password)
                {
                    this.errors.password = "Password can't be empty";
                    return false;
                }
                if(this.password.length < 8)
                {
                    this.errors.password = "Password must be at least 8 characters long";
                    return false;
                }
                let regex = /^[A-Za-z0-9!@#\$%\^&\*]+/;
                if(!regex.test(this.password))
                {
                    this.errors.password = "Password can contain alphabet letters, numbers and !@#$%^&* only";
                    return false;
                }
                return true;
            },
            validateConfPassword: function()
            {
                if(!this.confpassword)
                {
                    this.errors.confpassword = "Confirmation password cannot be empty";
                    return true;
                }
                if(this.confpassword.length < 8)
                {
                    this.errors.confpassword = "Confirmation password must be at least 8 letters long";
                    return false;
                }
                let regex = /^[A-Za-z0-9!@#\$%\^&\*]+/;
                let isValid = regex.test(this.confpassword) && (this.password === this.confpassword);
                if(!isValid)
                {
                    this.errors.confpassword = "Passwords must match";
                }
                return isValid;
            },
            validateInputs: function()
            {
                this.errors = 
                    {
                        username:     "",
                        password:     "",
                        confpassword: "",
                        name:         "",
                        surname:      "",
                        sex:          "",
                    };
                let nameValid = this.validateName();
                let surnameValid = this.validateSurname();
                let sexValid = this.validateSex();
                let usernameValid = this.validateUsername();
                let passwordValid = this.validatePassword();
                let confPasswordValid = this.validateConfPassword();
                return nameValid && surnameValid && sexValid && usernameValid
                    && passwordValid && confPasswordValid;
            },
            // TODO(Jovan): Validation
            registerHost: function()
            {
                this.successMsg = "";
                if(!this.validateInputs())
                {
                    return;
                }
                let host = 
                    {
                        username:     this.username,
                        password:     this.password,
                        name:         this.name,
                        surname:      this.surname,
                        sex:          this.sex,
                        userType:     "HOST",
                        apartments:   [],
                    };

                axios.post("rest/reg_host", host, {headers: {"Authorization": "Bearer " + localStorage.jwt}})
                    .then(response => 
                    {
                        // TODO(Jovan): Handle success
                        this.successMsg = "Registered";
                        this.$router.go();
                    })
                    .catch(response =>
                    {
                        // TODO(Jovan): Handle exception
                        this.successMsg = "Registration failed";
                    });
            },
            
            
            searchUser: function()
	        {
	        
	            if(!this.userUsername && !this.userSex && !this.userType)
	            {
					this.users = this.users_copy;
	            }
	            else if(!this.userUsername)
	            {
					if(!this.userType) // only sex            
	            	{
				        var result = [];
				        for(user of this.users_copy)
				        {
				        	if(user.sex == this.userSex)
				        	{
				        		result.push(user);
				        	}
				        }
						this.users = result;
					}
					else if(!this.userSex) // only type
					{
						var result = [];
				        for(user of this.users_copy)
				        {
				        	if(user.userType == this.userType)
				        	{
				        		result.push(user);
				        	}
				        }
						this.users = result;
					}
					else // sex and type
					{
						var result = [];
			    		for(user of this.users_copy)
			    		{
			    			if(user.sex == this.userSex && user.userType == this.userType)
			    			{
			    				result.push(user);
			    			}
			    		}
			    		this.users = result;
					}
				}
				else if(!this.userSex)
				{
					if(!this.userType) //only username
					{
						var result = [];
			    		for(user of this.users_copy)
			    		{
			    			if(user.username == this.userUsername)
			    			{
			    				result.push(user);
			    			}
			    		}
			    		this.users = result;
			    	}
			    	else  //username and type
			    	{
			    		var result = [];
			    		for(user of this.users_copy)
			    		{
			    			if(user.username == this.userUsername && user.userType == this.userType)
			    			{
			    				result.push(user);
			    			}
			    		}
			    		this.users = result;
			    	}
				}
				else if(!this.userType)  //username and sex
				{
					var result = [];
			    		for(user of this.users_copy)
			    		{
			    			if(user.username == this.userUsername && user.sex == this.userSex)
			    			{
			    				result.push(user);
			    			}
			    		}
			    		this.users = result;
				}
				else   //everything
				{
					var result = [];
		    		for(user of this.users_copy)
		    		{
		    			if(user.username == this.userUsername && user.sex == this.userSex && user.userType == this.userType)
		    			{
		    				result.push(user);
		    			}
		    		}
		    		this.users = result;
				}
	        },
 
        },
        mounted()
        {
            this.getUsers();
        }
    }
</script>

<style>
    .success
    {
        color: #f00;
        font-weight: 500;
        padding-bottom: 8px;
    }

    .error
    {
        display: block;
        color: #f00;
        font-weight: 500;
        padding-bottom: 8px;
    }
</style>