<template>
    <div class="container">
        <table>
            <tr>
                <th>Username</th>
                <th>Name</th>
                <th>Surname</th>
                <th>User Type</th>
            </tr>
            <tr v-for="user in users" :key="user">
                <td>{{user.username}}</td>
                <td>{{user.name}}</td>
                <td>{{user.surname}}</td>
                <td>{{user.userType}}</td>
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