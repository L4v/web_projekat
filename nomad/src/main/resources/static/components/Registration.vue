<template>
    <div id="registration">
        <div id="registrationForm">
            <!-- TODO(Jovan): Disable button? -->
            <h1>Registration</h1>
            <div v-if="errors.length" class="errors">
                <b>Please correct the following error(s):</b>
                <ul>
                    <li v-for="error in errors">{{error}}</li>
                </ul>
            </div>

            <div class="tab" :class="{'active': (activeTab === 0)}">
                <floating-label
                    v-model="name"
                    placeholder="Name"
                    type="text">
                <p class="error" v-if="errors.name">{{errors.name}}</p>
                </floating-label>
                <floating-label
                    v-model="surname"
                    placeholder="Surname"
                    type="text">
                </floating-label>
                <p class="error" v-if="errors.surname">{{errors.surname}}</p>
                <select name="sex" v-model="sex" required>
                    <option value="" disabled>Sex</option>
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                    <option value="OTHER">Other</option>
                    <option value="PRIVATE">Prefer not to say</option>
                </select>
                <p class="error" v-if="errors.sex">{{errors.sex}}</p>
                <button id="nextBtn" class="button-primary" @click="nextTab()">Next</button>
            </div>

            <div class="tab" :class="{'active': (activeTab === 1)}">
                <floating-label
                    v-model="username"
                    placeholder="Username"
                    type="text">
                </floating-label>
                <p class="error" v-if="errors.username">{{errors.username}}</p>
                <floating-label
                    v-model="password"
                    placeholder="Password"
                    type="password">
                </floating-label>
                <p class="error" v-if="errors.password">{{errors.password}}</p>
                <floating-label
                    v-model="confpassword"
                    placeholder="Confirm password"
                    type="password">
                </floating-label>
                <p class="error" v-if="errors.confpassword">{{errors.confpassword}}</p>
                <button class="button-primary" v-on:click="registerGuest()">Register</button>
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
                name:         "",
                surname:      "",
                sex:          "",
                username:     "",
                password:     "",
                confpassword: "",
                successMsg: "",
                exists: false,
                activeTab: 0,
                errors: [],
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
                        this.successMsg = "Registered";
                    })
                .catch(error =>
                    {
                        this.successMsg = "Already exists!";
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
                    this.errors.push("Name must be of alphabet characters only");
                }
                return isValid;
            },
            validateSurname: function()
            {
                let regex = /^[A-Z][a-z]+/;
                let isValid = regex.test(this.surname);
                if(!isValid)
                {
                    this.errors.push("Surname must be of alphabet characters only");
                }
                return isValid;
            },
            // TODO(Jovan): make async calls?
            validateUsername: async function()
            {
                axios.get("/rest/guest_get_username", { params: {"username": this.username} })
                    .then(function(response)
                    {
                        // NOTE(Jovan): Username already exists -> invalid
                        this.errors.push("Username must be unique");
                        return false;
                    })
                    .catch(function(response)
                    {
                        // NOTE(Jovan): Username doesn't exist -> valid
                        let regex = /^[A-Za-z]+/;
                        let isRegexValid = regex.test(this.username);
                        if(!isRegexValid)
                        {
                            this.errors.push("Username must contain alphabet letters only");
                            return false;
                        }
                        return true;
                    });
            },
            validatePassword: function()
            {
                if(this.password.len < 8)
                {
                    this.errors.push("Password must be at least 8 characters long");
                    return false;
                }
                let regex = /^[A-Za-z0-9!@#\$%\^&\*]+/;
                let isValid = regex.test(this.password);
                if(!isValid)
                {
                    this.errors.push("Password can only contain letters, numbers and !@#$%^&*");
                }
                return isValid;
            },
            validateConfPassword: function()
            {
                if(this.confpassword.len < 8)
                {
                    this.errors.push("Passwords must match");
                    return false;
                }
                let regex = /^[A-Za-z0-9!@#\$%\^&\*]+/;
                let isValid = regex.test(this.confpassword) && (this.password === this.confpassword);
                if(!isValid)
                {
                    this.errors.push("Passwords must match");
                }
                return isValid;
            },
            // TODO(jovan): Promises???
            validateInputs: async function()
            {
                this.errors = [];
                if(this.activeTab === 0)
                {
                    return (this.validateName()
                        && this.validateSurname());
                }
                else
                {
                    let usernameValid = await this.validateUsername();
                    let passwordValid = this.validatePassword() && this.validateConfPassword();
                    return usernameValid && passwordValid;
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
