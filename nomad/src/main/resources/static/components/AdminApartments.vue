<template>
	<div class="container">
		<div id="apartments">
			<h1>Apartments</h1>
			<br>
	       	<div id="filter">
	       		Filter:
	       		<select name="typeFilter" v-model="typeFilter" required>
	            	<option value="" disabled>Type</option>
	            	<option value="">ALL</option>
	            	<option value="WHOLE">WHOLE</option>
	            	<option value="ROOM">ROOM</option>
	       		</select>
	       		<select name="statusFilter" v-model="statusFilter" required>
	            	<option value="" disabled>Status</option>
	            	<option value="">ALL</option>
	            	<option value="ACTIVE">ACTIVE</option>
	            	<option value="INACTIVE">INACTIVE</option>
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
					<th>ID</th>
					<th>Address</th>
					<th>Type</th>
					<th @click="sort('price')">
                        Price
                        <span v-if="sortBy === 'price'">
                            <span v-if="sortOrder < 0"><i class="fa fa-chevron-down" aria-hidden="true"></i></span>
                            <span v-else><i class="fa fa-chevron-up" aria-hidden="true"></i></span>
                        </span>
                    </th>
					<th>Status</th>
					<th>Host username</th>
				</tr>
				 <tr v-for="apartment in sortedList">
				 	<td>{{apartment.id}}</td>
				 	<td>{{apartment.location.address.street}} {{apartment.location.address.streetNumber}} {{apartment.location.address.area}}</td>
					<td>{{apartment.type}}</td>
					<td>{{apartment.price}}</td>
					<td>{{apartment.status}}</td>
					<td>{{apartment.hostId}}</td>
				</tr>
			</table>
			<button class="button-primary" @click="removeAll()">Remove all</button>
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
				typeFilter: "",
				statusFilter: "",
				allAmenities:      [],
                selectedAmenities: [],
                leftSelected:      [],
                rightSelected:     [],
                sortOrder: 1,
                sortBy: "price",
			}
		},
		
		mounted()
		{
	    	let jwt = localStorage.jwt;
	    	
	    	
	    	if(jwt)
			{
		    	axios.get("rest/admin_all_apartments", {headers:{"Authorization": "Bearer " + jwt}})
			        .then(response => 
			       	{
			       		this.apartments = response.data;
			       		this.apartments_copy = response.data;
			       	})
		    		.catch(response => 
	    			{
	    			});
	    			
	    		axios.get("rest/admin_all_amenities", {headers:{"Authorization": "Bearer " + jwt}})
			        	.then(response => (this.allAmenities = response.data))
		    		.catch(response => 
	    			{
	    			});
			}
	    },
	    
	    methods:
	    {
			removeAll: function()
	     	{
				let jwt = localStorage.jwt;
				if(!jwt)
				{
					this.$router.go();
					return;
				}

	     		axios.post("rest/admin_remove_apartments", {}, {headers:{"Authorization": "Bearer " + jwt}})
			        .then(response =>
		            {
		            	this.apartments = response.data;
		                this.successMsg = "Apartments successfully removed.";
		            })
			        .catch(response => {
			        		this.successMsg = "Failed removing apartments";
			        });
	     	},
	     	
	     	sort: function(sortBy)
            {
                if(this.sortBy === sortBy)
                {
                    this.sortOrder = -this.sortOrder;
                }
                else
                {
                    this.sortBy = sortBy;
                }
            },
	     	
	     	filterApartments: function()
	     	{
	     		if(!this.typeFilter && !this.statusFilter && this.selectedAmenities.length == 0)
	     		{
	     			this.apartments = this.apartments_copy;
	     		}
	     		else if(!this.typeFilter)
	     		{
	     			if(!this.statusFilter) //just amenities
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
	     			else if(this.selectedAmenities.length == 0) //just status
	     			{
		     			var retVal = [];
		     			for(apartment of this.apartments_copy)
		     			{
		     				if(apartment.status == this.statusFilter)
		     				{
		     					retVal.push(apartment);
		     				}
		     			}
	     				this.apartments = retVal;
	     			}
	     			else     //amenities and status
	     			{
	     				var retVal = [];
		     			for(apartment of this.apartments_copy)
		     			{
		     				if(apartment.status == this.statusFilter)
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
				} 
	     		else if(!this.statusFilter)
	     		{
	     			if(this.selectedAmenities.length == 0) //just type
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
		     		else   //amenities and type
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
	     		}
	     		else if(this.selectedAmenities.length == 0)  //status and type
	     		{
	     			var retVal = [];
	     			for(apartment of this.apartments_copy)
	     			{
	     				if(apartment.status == this.statusFilter && apartment.type == this.typeFilter)
	     				{
	     					retVal.push(apartment);
	     				}
	     			}
	     			this.apartments = retVal;
	     		}
	     		else   //everything
	     		{
	     			var retVal = [];
	     			for(apartment of this.apartments_copy)
	     			{
	     				if(apartment.status == this.statusFilter && apartment.type == this.typeFilter)
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
	    
	     computed:
        {
            sortedList: function()
            {
                let sortBy = this.sortBy;
                return [...this.apartments]
                    .sort((a, b) => a[sortBy] > b[sortBy] ? this.sortOrder : -this.sortOrder);
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