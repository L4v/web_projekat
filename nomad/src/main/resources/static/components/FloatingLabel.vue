<template>
    <!-- NOTE(Jovan):
        Input field with floating placeholders

        To simulate 'v-model', use :inputdata.sync
        Example:
        <floating-label :inputdata.sync="parentData" type="text" placeholder="test"></floating-label>
        
        options:
            placeholder - placeholder text
            name        - defines input name
            type        - defines input type (password or text)
    -->
            <!-- @input="$emit('input', $event.target.value); isEmpty($event)" -->
    <div class="loginField floating-label" ref="inputdiv">
        <!-- TODO(Jovan): Solve warning??? -->
        <label for="username">{{placeholder}}</label>
        <input 
            :name="name"
            :type="type"
            @blur="removeFocus($event)"
            @focus="addFocus($event)"
            v-model="inputdata"
            @input="$emit('update:inputdata', inputdata)"
            />
    </div>
</template>
<script>
    module.exports = {
        props: ["placeholder", "name", "type", "inputdata"],
        methods:
        {
            // NOTE(Jovan): If there is a value in input when mounting,
            //              add 'focused' class
            addFocus: function(e)
            {
                let classList = e.target.parentElement.classList;
                if(!classList.contains('focused'))
                {
                    classList.toggle('focused');
                }
            },
            removeFocus: function(e)
            {
                let classList = e.target.parentElement.classList;
                if(classList.contains('focused') && this.isEmpty())
                {
                    classList.toggle('focused');
                }
            },
            isEmpty: function(e)
            {
                if(this.inputdata)
                {
                    return false;
                }
                return true;
            },
            addFocusToDiv: function()
            {
                if(this.inputdata)
                {
                    this.$refs["inputdiv"].classList.add("focused");
                }
            }
        },
        mounted()
        {
            // NOTE(Jovan): If floating-label is loaded with predefined values
            this.addFocusToDiv();
        },
        watch:
        {
            // NOTE(Jovan): Same as mounted call, only used for axios' async calls
            inputdata: function(newVal, oldVal)
            {
                if(!oldVal && newVal)
                {
                    this.addFocusToDiv();
                }
            },
        },
    }
</script>
<style scoped>

    .floating-label
    {
        position: relative;
        margin-bottom: 10px;
    }

    .floating-label label
    {
        transition: all .2s ease-in-ease-out;
        font-size: 1.5rem;
        pointer-events: none;
        position: absolute;
        white-space: nowrap;
        overflow: hidden;
        top: 0;
        display: inline-block;
        background: #fff;
        margin: 8px 5px;
        padding: 0 10px;
        opacity: 0.6;
        font-weight: 500;
		text-transform: capitalize;
    }

    .focused label
    {
        transition: all .2s linear;
        opacity: 1;
        color: #ff5722;
        top: -20px;
        font-size: 1.1rem;
		text-transform: uppercase;
    }
</style>
