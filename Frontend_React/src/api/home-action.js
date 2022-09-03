const axios = require('axios');


export const getWeatherConditions = async () => {
    try {
        const result = await axios.get('https://api.data.gov.sg/v1/environment/2-hour-weather-forecast');
        const weatherRes = JSON.parse(result.request.response)
        
        //return {
        //    current_datetime: weatherRes.items[0].timestamp,
        //    condtion: weatherRes.items[0].forecasts[11].forecast
        //}
        
        return weatherRes.items[0];

    } catch (error) {
        console.error(error);
    }
}