<template>
    <div id="registration">
        <div id="registrationForm">
            <!-- TODO(Jovan): Disable button? -->
            <h1>Registration</h1>
            <b class="error">{{registrationMsg}}</b>

            <div class="tab" :class="{'active': (activeTab === 0)}">
                <small class="error">{{errors.name}}</small>
                <floating-label
                    :inputdata.sync="name"
                    placeholder="Name"
                    type="text">
                </floating-label>
                <small class="error">{{errors.surname}}</small>
                <floating-label
                    :inputdata.sync="surname"
                    placeholder="Surname"
                    type="text">
                </floating-label>
                <small class="error">{{errors.sex}}</small>
                <select name="sex" v-model="sex" required>
                    <option value="" disabled>Sex</option>
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                    <option value="OTHER">Other</option>
                    <option value="PRIVATE">Prefer not to say</option>
                </select>
                <button id="nextBtn" class="button-primary" @click="nextTab()">Next</button>
            </div>

            <div class="tab" :class="{'active': (activeTab === 1)}">
                <small class="error">{{errors.username}}</small>
                <floating-label
                    :inputdata.sync="username"
                    placeholder="Username"
                    type="text">
                </floating-label>
                <small class="error">{{errors.password}}</small>
                <floating-label
                    :inputdata.sync="password"
                    placeholder="Password"
                    type="password">
                </floating-label>
                <small class="error">{{errors.confpassword}}</small>
                <floating-label
                    :inputdata.sync="confpassword"
                    placeholder="Confirm password"
                    type="password">
                </floating-label>
                <button class="button-primary" @click="registerGuest()">Register</button>
                <button id="prevBtn" @click="prevTab">Previous</button>
            </div>
        </div>

        <div>
            <span class="step"></span> <span class="step"></span>
        </div>
    </div>
</template>

<script>
    module.exports = {
        data: function()
        {
            return{
                name:            "",
                surname:         "",
                sex:             "",
                username:        "",
                password:        "",
                confpassword:    "",
                exists: false,
                activeTab: 0,
                registrationMsg: "",
                errors: 
                    {
                        name:         "",
                        surname:      "",
                        sex:          "",
                        username:     "",
                        password:     "",
                        confpassword: "",
                    },
            }
        },
        methods:
        {
            registerGuest: async function()
            {
                let validInputs = await this.validateInputs();
                if(!validInputs)
                {
                    return;
                }
                let guest =
                {
                    name:     this.name,
                    surname:  this.surname,
                    sex:      this.sex,
                    username: this.username,
                    password: this.password,
                    userType: "GUEST",
                };
                const vm = this;
                axios.post("rest/reg_guest", guest)
                .then(response =>
                    {
                        this.registrationMsg = "Registered";
                    })
                .catch(error =>
                    {
                        this.registrationMsg = "Username already taken";
                        this.exists = true;
                    });
            },
            nextTab: function()
            {
                if(!this.validateInputs())
                {
                    return;
                }
                this.activeTab = this.activeTab < 1 ? ++this.activeTab : this.activeTab;
            },
            prevTab: function()
            {
                this.activeTab = this.activeTab > 0 ? --this.activeTab : this.activeTab;
            },
            validateName: function()
            {
                let regex = /^[A-Z][a-z]+/;
                let isValid = regex.test(this.name);
                if(!isValid)
                {
                    this.errors.name = "Name can contain alphabet letters only and must be uppercase";
                }
                return isValid;
            },
            validateSurname: function()
            {
                let regex = /^[A-Z][a-z]+/;
                let isValid = regex.test(this.surname);
                if(!isValid)
                {
                    this.errors.surname = "Surname can contain alphabet letters only and must be uppercase";
                }
                return isValid;
            },
            validateSex: function()
            {
                if(!this.sex)
                {
                    this.errors.sex = "Please select your sex";
                    return false;
                }
                return true;
            },
            // TODO(Jovan): make async calls?
            // TODO(Jovan): Check existing username
            validateUsername: async function()
            {
                if(!this.username)
                {
                    this.errors.username = "Username cannot be empty";
                    return false;
                }

                let regex = /^[A-Za-z]+/;
                if(!regex.test(this.username))
                {
                    this.errors.username = "Username must contain alphabet letters only";
                    return false;
                }
                return true;
            },
            validatePassword: function()
            {
                if(!this.password)
                {
                    this.errors.password = "Password cannot be empty";
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
                    this.errors.password = "Password can only contain letters, numbers and !@#$%^&*";
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
            // TODO(jovan): Promises???
            validateInputs: function()
            {
                this.errors =
                    {
                        name:         "",
                        surname:      "",
                        sex:          "",
                        username:     "",
                        password:     "",
                        confpassword: "",
                    };
                this.errors = [];
                if(this.activeTab === 0)
                {
                    let nameValid = this.validateName();
                    let surnameValid = this.validateSurname();
                    let sexValid = this.validateSex();
                    return nameValid && surnameValid && sexValid;
                }
                else
                {
                    let usernameValid = this.validateUsername();
                    let passwordValid = this.validatePassword() 
                    let confPasswordValid = this.validateConfPassword();
                    return usernameValid && passwordValid && confPasswordValid;
                }
            }
        },
        created: function()
        {
            // TODO(jovan): Only for debugging
            console.log("created: Registration.vue");
        },
        /* TODO(Jovan): Use watchers?
        watch:
        {
            name(value)
            {
                this.name = value;
                if(this.validateName(value))
                {
                    this.errors["name"] = "";
                }
                else
                {
                    this.errors["name"] = "Invalid name. Must be a single, capitalized word";
                }
            },
            surname(value)
            {
                this.surname = value;
                if(this.validateName(value))
                {
                    this.errors["surname"] = "";
                }
                else
                {
                    this.errors["surname"] = "Invalid surname. Must be a single, capitalized word";
                }
            },
            username(value)
            {
                this.username = value;
                if(this.validateUsername(value))
                {
                    this.errors["username"] = "";
                }
                else
                {
                    this.errors["username"] = "Invalid username. Must be unique and out of letters only";
                }
            },
            password(value)
            {
                this.password = value;
                if(this.validatePassword(value))
                {
                    this.errors["password"] = "";
                }
                else
                {
                    this.errors["password"] = "Must be at least 8 characters long, can contain letters, numbers and !@#$%^&*";
                }
            },
            confpassword(value)
            {
                this.confpassword = value;
                if(this.validatePassword(value))
                {
                    this.errors["confpassword"] = "";
                }
                else
                {
                    this.errors["confpassword"] = "Must match password";
                }
            },
        }
        */
    }
</script>

<style scoped>

    #registration
    {
        position: fixed;
        width: 100vw;
        height: 100vh;
        background: none;
        background-color: rgb(245, 245, 245);
    }

    #registrationForm
    {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;

        margin: auto;
        margin-top: 5vh;
        width: 25%;
        height: 60vh;

        border: 1px solid rgba(127, 127, 127, 0.5);
        border-radius: 20px;

        background: none;
        background-color: #fff;
        box-shadow: 0 10px 20px rgba(0,0,0,0.19), 0 6px 6px rgba(0,0,0,0.23);
    }

    .error
    {
        color: #f00;
        padding: 0;
        padding-bottom: 9px;
        margin: 0;
        font-weight: 500;
    }

    .nav-buttons
    {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }
    
    .tab
    {
        display: none;
    }

    .tab.active
    {
        display: flex;
        flex-direction: column;
    }

    .regFail {
        color: #f00;
    }

    .regSuccess {
        color: #0f0;
    }
</style>
