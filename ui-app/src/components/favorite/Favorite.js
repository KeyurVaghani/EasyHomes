import React, {useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import "./Favorite.css"

function Favorite() {
    let data = {
        favorites: [
            {
                id: 1,
                propertyName: '3569 Dutch Village Rd Halifax - Apartments For Rent In Halifax',
                date: 'February 2022',
                content: 'Apartments for rent in Halifax with over 1,100 sq ft of living space. 2 bedroom 2 bathroom. Open concept kitchen, stainless steel appliances, in-suite laundry 2 bed 2 bath apartments. Call now. Book a Viewing. Styles: 1 Bedroom, 2 Bedroom, 2 Bed 2 Bath Apartments.',
                img:"https://s3.amazonaws.com/lws_lift/panoramic/images/gallery/1152/1612967836_img_8991.png"
            },
            {
                id: 2,
                propertyName: 'Condos in Halifax - Largest Selection, Best Prices',
                date: 'January 2022',
                content: 'Book the Perfect Vacation Condo in Halifax with up to 75% Discount! Compare the Best Vacation Condos in Halifax from the Largest Selection. Millions of Offers. World of Vacation Rentals. Lowest Prices. Most Properties. Mobile-friendly. From Trusted Partners. Best Deals. Metasearch Engine. Amenities: WiFi, TV, AC, Pool, Kitchen, Balcony, BBQ, Terrace, Sauna, Patio.',
                img:"https://i.ebayimg.com/images/g/sowAAOSw4rpiHokh/s-l640.webp"
            },
            {
                id: 3,
                propertyName: '172 NORTH - 2 Bedroom + Den Apartment for Rent',
                date: 'December 2021',
                content: '172 Wentworth Dr- Penthouse\n' +
                    '\n' +
                    'Experience all the comforts of contemporary living. Enjoy the seclusion and comfort of a modern urban village, mere minutes away from the best of Halifax.',
                img:"https://i.ebayimg.com/images/g/Z~sAAOSwPjpiJqIs/s-l640.webp"
            },
            {
                id: 4,
                propertyName: '172 NORTH - 3 Bedroom Apartment for Rent',
                date: 'December 2021',
                content: '172 Wentworth Dr\n' +
                    '\n' +
                    'Experience all the comforts of contemporary living. Enjoy the seclusion and comfort of a modern urban village, mere minutes away from the best of Halifax.',
                img:"https://i.ebayimg.com/images/g/2kkAAOSwCbFiJqhX/s-l640.webp"
            }
        ]
    };
    let newData = {
        newFavorites: []
    };
    let navigate = useNavigate();

    const {state} = useLocation();

    const [query, setQuery] = useState("")

    if (state != null) {
        data.favorites.map(item => {
            if (state.id === item.id) {
                if (item.content !== state.content && state.content.length !== 0) {
                    item.content = state.content;
                }
            }
        })
    }

    function deleteDiv(id) {
        let div = document.getElementById(id);
        let divParent = div.parentElement;
        if (divParent !== null) {
            divParent.removeChild(div);
        }
    }

    return (
        <div>
            <div className="favorite-page">
                <div className="body">
                    <section className="left">
                        <figure className="profile">
                            <img className="profile-img" src="https://th.bing.com/th/id/OIP.4_I4r2WoSSAiTKs4jM97lQHaHa?pid=ImgDet&rs=1" alt="profile photo"/>
                            <figcaption>xxxx xxxx</figcaption>
                        </figure>
                        <menu className="profile-menu-bar">
                            <button className="profile-menu" type="button">Account Info</button>
                            <button className="profile-menu" type="button">Favorites List</button>
                        </menu>
                    </section>
                    <section className="right">
                        <section className="search-bar">
                            <input type="text" className="search-content" placeholder="Search properties" onChange={event => setQuery(event.target.value)}/>
                        </section>
                        <main className="favorites" name="favorites">
                            <h1>My Favorites List</h1>
                            {data.favorites.filter(item => {
                                if (query === '') {
                                    return item;
                                } else if (item.propertyName.toLowerCase().includes(query.toLowerCase())||item.content.toLowerCase().includes(query.toLowerCase())) {
                                    return item;
                                }
                            }).map(item => (
                                <div key={item.id} className="favorite-content" id={item.id}>
                                    <h3>{item.propertyName}</h3>
                                    <hr/>
                                    <p>{item.content}</p>
                                    <img src={item.img} alt="property photo" className="property-img"  onClick={() => {
                                        navigate('/propertydetail', {
                                            state: {
                                                id: item.id,
                                                date: item.date,
                                                propertyName: item.propertyName,
                                                content: item.content,
                                                img: item.img
                                            }
                                        })
                                    }}
                                    />
                                    <div className="favorite-buttons">
                                        <button className="favorite-btn" type="button" onClick={() => {
                                            deleteDiv(item.id);
                                        }}>Cancel
                                        </button>
                                    </div>
                                </div>
                            ))}
                        </main>
                    </section>
                </div>
            </div>
        </div>
    );
}

export default Favorite;