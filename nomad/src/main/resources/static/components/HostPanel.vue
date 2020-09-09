<template>
    <div>
        <sidebar title="Host panel" :user="user">
            <li>
                <router-link to="#">Overview</router-link>
                <router-link to="/host/apartments>Apartments</router-link>
                <router-link to="/host/guests>Guests</router-link>
                <router-link to="/host/personal_data>Personal data</router-link>
            </li>
        </sidebar>
        <div id="main-content">
        </div>
    </div>
</template>

<script>
</script>

<style scoped>
    #main-content
    {
        margin-left: 15vw;
        background-color: #fafafa;
        height: 100vh;
    }
</style>