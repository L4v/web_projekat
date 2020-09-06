<template>
    <div>
        <!-- TODO(Jovan): Make into a separate component -->
        <div id="sidebar">
            <b id="sidebar-title">Admin panel</b>
            <hr/>
            <ul>
                <li><router-link to="/admin/overview">Overview</router-link></li>
                <li><router-link to="#">Placeholder 1</router-link></li>
                <li><router-link to="#">Placeholder 2</router-link></li>
                <li><router-link to="#">Placeholder 3</router-link></li>
            </ul>
        </div>

        <div id="main-content">
            <router-view></router-view>
        </div>
    </div>
</template>

<style scoped>
    #sidebar
    {
        height: 100%;
        width: 10vw;
        position: fixed;
        z-index: 1;
        top: 0;
        left: 0;
        background-color: #2c3e50;
        overflow-x: hidden;
        padding-top: 20px;
    }

    #sidebar-title
    {
        display: block;
        color: #ecf0f1;
        font-weight: 500;
        font-size: 1.8rem;
        text-align: center;
        padding: 8px 16px 8px 16px;
    }

    #sidebar a
    {
        display: block;
        color: #fff;
        padding: 4px 8px 4px 8px;
    }
    #sidebar a:hover
    {
        color: #ff5722;
        font-weight: 500;
        background-color: #34495e;
    }

    #main-content
    {
        margin-left: 10vw;
    }
    
</style>
