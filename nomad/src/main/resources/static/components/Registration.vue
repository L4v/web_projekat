<template>
    <div id="registration">
        <div id="registrationForm">
            <h1>Registration</h1>
            <h2 v-bind:class="exists ? 'regFail' : 'regSuccess'">{{successMsg}}</h2>

            <div class="tab">
                <floating-label
                    v-model="guest.name"
                    placeholder="Name"
                    type="text">
                </floating-label>
                <floating-label
                    v-model="guest.surname"
                    placeholder="Surname"
                    type="text">
                </floating-label>
                <select name="sex" v-model="guest.sex">
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                    <option value="OTHER">Other</option>
                    <option value="PRIVATE">Prefer not to say</option>
                </select>
            </div>

            <div class="tab">
                <floating-label
                    v-model="guest.username"
                    placeholder="Username"
                    type="text">
                </floating-label>
                <floating-label
                    v-model="guest.password"
                    placeholder="Password"
                    type="password">
                </floating-label>
                <floating-label
                    v-model="guest.password"
                    placeholder="Confirm password"
                    type="password">
                </floating-label>
                <button class="button-primary" v-on:click="registerGuest()">Register</button>
            </div>

        </div>

        <div>
            <button id="prevBtn">Previous</button>
            <button id="nextBtn" class="button-primary">Next</button>
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
                guest: {},
                successMsg: "",
                exists: false,
            }
        },
        methods:
        {
            registerGuest: function()
            {
                let g = 
                    {
                        username: this.guest.username,
                        password: this.guest.password,
                        name:     this.guest.name,
                        surname:  this.guest.surname,
                        sex:      this.guest.sex,
                        userType: "GUEST",
                    };
                const vm = this;
                axios.post("rest/reg_guest", g)
                .then(response =>
                    {
                        this.successMsg = "Registered";
                    })
                .catch(error =>
                    {
                        this.successMsg = "Already exists!";
                        this.exists = true;
                    });
            }
        }
    }
</script>

<style scoped>
    .regFail {
        color: #f00;
    }

    .regSuccess {
        color: #0f0;
    }
</style>
