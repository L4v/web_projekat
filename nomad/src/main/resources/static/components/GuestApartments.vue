<template>
	<div class="container">
		<div id="apartments">
			<h1>Apartments</h1>
			<br>
			<div id="sort">
				Sort by:
				<select name="sortType" v-model="sortType" required>
	            	<option value="" disabled>Sort</option>
	            	<option value="PRICE">PRICE</option>
	            	<option value="NUMBER_OF_ROOMS">NUMBER OF ROOMS</option>
	            	<option value="NUMBER_OF_GUESTS">NUMBER OF GUESTS</option>
	       		</select>
				<select name="sort" v-model="sort" required>
	            	<option value="" disabled>Sort</option>
	            	<option value="ASCENDING">Ascending</option>
	            	<option value="DESCENDING">Descending</option>
	       		</select>
	       		<button class="button-primary" @click=sortApartments()>Sort</button>
       		</div>
       		<div id="search">
       			Filter:
       			<select name="typeFilter" v-model="typeFilter" required>
	            	<option value="" disabled>Type</option>
	            	<option value="">ALL</option>
	            	<option value="WHOLE">WHOLE</option>
	            	<option value="ROOM">ROOM</option>
	       		</select>
	       		<div id="amenities">
                        <select id="leftSelect" name="allAmenities" v-model="leftSelected"  multiple>
                            <option v-for="amenity in allAmenities" :value="amenity">{{amenity.name}}</option>
                        </select>
                        <div id="amenity-buttons">
                            <button type="button" @click="addAmenity">&rsaquo;</button>
                            <button type="button" @click="removeAmenity">&lsaquo;</button>
                            <button type="button" @click="clearAmenities">Clear all</button>
                            <button type="button" @click="addAllAmenities">Add all</button>
                        </div>
                        <select id="rightSelect" name="selectedAmenities"  v-model="rightSelected" multiple>
                            <option v-for="amenity in selectedAmenities" :value="amenity">{{amenity.name}}</option>
                        </select>
                   </div>
	       		<button class="button-primary" @click=filterApartments()>Filter</button>
       		</div>
       		<br>
			<table>
				<tr>
					<th>Address</th>
					<th>Type</th>
					<th>No. of rooms</th>
					<th>No. of guests</th>
					<th>Price</th>
					<th>More information</th>
				</tr>
				 <tr v-for="apartment in apartments">
				 	<td>{{apartment.location.address.street}} {{apartment.location.address.streetNumber}} {{apartment.location.address.area}}</td>
					<td>{{apartment.type}}</td>
					<td>{{apartment.noRooms}}</td>
					<td>{{apartment.noGuests}}</td>
					<td>{{apartment.price}}</td>
					<td><router-link :to="{ name: 'ShowApartment', params: { apartment } }">More</router-link></td>
				</tr>
			</table>
		</div>
	</div>
</template>

<script>
	module.exports = {
		data: function()
		{
			return {
				apartments: {},
				apartments_copy: {},
				successMsg: "",
				sort: "",
				sortType: "",
				typeFilter: "",
				allAmenities:      [],
                selectedAmenities: [],
                leftSelected:      [],
                rightSelected:     [],
			}
		},
		
		mounted()
		{
	    	var jwt = localStorage.jwt;
	    	
	    	if(jwt)
			{
		    	axios.get("rest/guest_reserved_apartments", {headers:{"Authorization": "Bearer " + localStorage.jwt}})
			        .then(response => 
			       	{
			       		this.apartments = response.data;
			       		this.apartments_copy = response.data;
			       	})
	    			
	    		axios.get("rest/get_amenities")
                    .then(response =>
                    {
                        this.allAmenities = response.data;
                    });
			}
	    },
	    
	    methods:
	    {
	    
	    	sortApartments: function()
	    	{
	    		if((!this.sort && !this.sortType) || (this.sort == 'DESCENDING' && !this.sortType) || (this.sort == 'ASCENDING' && !this.sortType))
	    		{
	    			successMsg = "Sort parameters must not be empty.";
	    		}
	    		else if(this.sort == 'DESCENDING')	
	    		{	
	    			if(this.sortType == 'PRICE')
	    			{
	    				this.apartments.sort((a, b) => (a.price > b.price) ? 1 : -1);
	    			}
	    			else if(this.sortType == 'NUMBER_OF_ROOMS')
	    			{
	    				this.apartments.sort((a, b) => (a.noRooms > b.noRooms) ? 1 : -1);
	    			}
	    			else if(this.sortType == 'NUMBER_OF_GUESTS')
	    			{
	    				this.apartments.sort((a, b) => (a.noGuests > b.noGuests) ? 1 : -1);
	    			}
	    		}
	    		else if(this.sort == 'ASCENDING')
	    		{
	    			if(this.sortType == 'PRICE')
	    			{
	    				this.apartments.sort((a, b) => (a.price < b.price) ? 1 : -1);
	    			}
	    			else if(this.sortType == 'NUMBER_OF_ROOMS')
	    			{
	    				this.apartments.sort((a, b) => (a.noRooms < b.noRooms) ? 1 : -1);
	    			}
	    			else if(this.sortType == 'NUMBER_OF_GUESTS')
	    			{
	    				this.apartments.sort((a, b) => (a.noGuests < b.noGuests) ? 1 : -1);
	    			}
	    		}	
	    	},
	    	
	    	filterApartments: function()
	     	{
	     		if(!this.typeFilter && this.selectedAmenities.length == 0)
	     		{
	     			this.apartments = this.apartments_copy;
	     		} 
	     		else if(this.selectedAmenities.length == 0)
	     		{
	     			var retVal = [];
	     			for(apartment of this.apartments_copy)
	     			{
	     				if(apartment.type == this.typeFilter)
	     				{
	     					retVal.push(apartment);
	     				}
	     			}
	     			this.apartments = retVal;
	     		}
	     		else if(!this.typeFilter)
	     		{
	     			var retVal = [];
	     			for(apartment of this.apartments_copy)
	     			{
	     				for(selectedAmenity of this.selectedAmenities)
	     				{
	     					if(apartment.amenities.some(amenity => amenity.id === selectedAmenity.id))
	     					{
	     						retVal.push(apartment);
	     						break;
	     					}
	     				}
	     			}
     				this.apartments = retVal;
	     		}
	     		else
	     		{
	     			var retVal = [];
	     			for(apartment of this.apartments_copy)
	     			{
	     				if(apartment.type == this.typeFilter)
	     				{
	     					for(selectedAmenity of this.selectedAmenities)
	     					{
		     					if(apartment.amenities.some(amenity => amenity.id === selectedAmenity.id))
		     					{
		     						retVal.push(apartment);
		     						break;
		     					}
	     					}
	     				}
	     			}
	     			this.apartments = retVal;
	     		
	     		}
	     	},
	     	
	     	addAmenity: function()
            {
                let selected = this.leftSelected;
                if(!selected)
                {
                    return;
                }
                console.log("adding " + selected);
                this.selectedAmenities = this.selectedAmenities.concat(selected);

                selected.forEach(el => 
                {
                    let index = this.allAmenities.indexOf(el);
                    this.allAmenities.splice(index, 1); 
                });
                this.leftSelected = [];
            },

            removeAmenity: function()
            {
                let selected = this.rightSelected;
                if(!selected)
                {
                    return;
                }
                this.allAmenities = this.allAmenities.concat(selected);

                selected.forEach(el =>
                {
                    let index = this.selectedAmenities.indexOf(el);
                    this.selectedAmenities.splice(index, 1);
                });

                this.rightSelected = [];
            },

            clearAmenities: function()
            {
                this.allAmenities = this.selectedAmenities.concat(this.allAmenities);
                this.selectedAmenities = [];
            },

            addAllAmenities: function()
            {
                this.selectedAmenities = this.allAmenities.concat(this.selectedAmenities);
                this.allAmenities = [];
            },      
	    	
	    },
    }
</script>

<style>
	#amenities
    {
        display: flex;
        flex-direction: row;
        flex: 2 1 2;
    }

    #amenities select
    {
        height: 100%;
        min-height: 200px;
        min-width: 200px;
    }

    #amenity-buttons
    {
        display: flex;
        flex-direction: column;
    }
</style>