import React, { useEffect, useReducer, useContext } from 'react';
// @mui
import { useTheme } from '@mui/material/styles';
import { Grid, Container, Typography, Snackbar, Alert } from '@mui/material';
import AppWidgetSummary from './AppWidgetSummary'
import AppEventTimeline from './AppEventTimeline'
import AppWebsiteVisits from './AppWebsiteVisits'
import AppCurrentVisits from './AppCurrentVisits'
import Loading from './../shared/Loading';
import GroupHome from '../group/GroupHome';

import DashboardDataService from '../../api/dashboard-actions'
import GroupDataService from '../../api/group-actions';
import { DashboardProvider } from '../../providers/dashboardProvider'
import { TabContext } from '../../providers/tabProvider'
import { dashboardReducer, initialState } from '../../reducers/dashboardReducer'
import { Claims } from '../../utils/ClientCache';

const stringToColor = (string) => {
    let hash = 0;
    let i;

    /* eslint-disable no-bitwise */
    for (i = 0; i < string.length; i += 1) {
        hash = string.charCodeAt(i) + ((hash << 5) - hash);
    }

    let color = '#';

    for (i = 0; i < 3; i += 1) {
        const value = (hash >> (i * 8)) & 0xff;
        color += `00${value.toString(16)}`.slice(-2);
    }
    /* eslint-enable no-bitwise */

    return color;
}

const Dashboard = () => {
    document.body.style.overflowY = 'scroll';
    const theme = useTheme();
    const [dashboardContext, dispatch] = useReducer(dashboardReducer, initialState);

    useEffect(() => {
        const initDashboardData = async () => {
            dispatch({ type: 'SET_LOADING', loading: true });
            GroupDataService.getGroups()
                .then((response) => {
                    const items = response.data;
                    dispatch({ type: 'SET_GROUP_LIST', groupList: items });
                    getDashboardData(items.length);
                })
                .catch((e) => {
                    console.log(e.response);
                    inputSnackBarDetails("Something went wrong!","error");
                    //alert("Something went wrong!");
                });
        };
        const getDashboardData = (totalGroupJoined) => {
            DashboardDataService.getDashboardData()
                .then((response) => {
                    const items = response.data;
                    const totalPublicGroup = (totalGroupJoined - items.totalGroups);
                    const participantRate = loadParticipantRate(items.participantRate);
                    dispatch({ type: 'SET_TOTAL_MESSAGE_SEND', totalMessageSend: items.totalMessageSend });
                    dispatch({ type: 'SET_TOTAL_EVENTS', totalEvents: items.totalEvents });
                    dispatch({ type: 'SET_TOTAL_PUBLIC_GROUP', totalPublicGroup });
                    dispatch({ type: 'SET_PARTICIPANT_RATE', participantRate });
                    dispatch({ type: 'SET_TOTAL_BOOK_GENRE', totalBookGenre: items.totalBookGenre });
                    dispatch({ type: 'SET_LOADING', loading: false });
                })
                .catch((e) => {
                    console.log(e.response);
                    inputSnackBarDetails("Something went wrong!","error");
                    //alert("Something went wrong!");
                });
        }

        initDashboardData();
    }, []);

    const loadParticipantRate = (items) => {
        let participantRateList = []
        items.map((data) => {
            let label = data.groupName;
            let value = data.participantCount;
            participantRateList.push({ label, value })
        })
        return participantRateList;
    };

    const handleSnackBarClose = () => {
        dashboardContext.snackBarDetails.open = false;
        dispatch({ type: 'SET_SNACKBARDETAILS', snackBarDetails: dashboardContext.snackBarDetails });
    };

    const inputSnackBarDetails = (message, type) => {
        dashboardContext.snackBarDetails = {
            open: true,
            message,
            type
        };
        dispatch({ type: 'SET_SNACKBARDETAILS', snackBarDetails: dashboardContext.snackBarDetails });
    }

    if (dashboardContext.loading) return <div style={{ marginBottom: '20px' }}><Container> <Loading /></Container></div>;

    return (
        <div style={{ borderTop: '1px solid rgb(25, 118, 210)' }}>
            <DashboardProvider value={{ dashboardContext, dispatch }}>
                <Container maxWidth="xl" sx={{ mb: 5 }}>
                    <Typography variant="h4" sx={{ mb: 3, mt: 3 }}>
                        Hi, Welcome back {Claims.getDisplayName()}
                    </Typography>

                    <Grid container spacing={3}>
                        <Grid item xs={12} sm={6} md={3}>
                            <AppWidgetSummary title="Public Group(s) Joined" total={dashboardContext.totalPublicGroup} icon={'group-outlined'} />
                        </Grid>

                        <Grid item xs={12} sm={6} md={3}>
                            <AppWidgetSummary title="Message(s) Send" total={dashboardContext.totalMessageSend} color="info" icon={'message-outlined'} />
                        </Grid>

                        <Grid item xs={12} sm={6} md={3}>
                            <AppWidgetSummary title="Recommended Book Category" total={dashboardContext.totalBookGenre} color="warning" icon={'category-outlined'} />
                        </Grid>

                        <Grid item xs={12} sm={6} md={3}>
                            <AppWidgetSummary title="Events Created" total={dashboardContext.totalEvents} color="error" icon={'person-outlined'} />
                        </Grid>

                        <Grid item xs={12} md={6} lg={4}>
                            <AppCurrentVisits
                                title="Participant rate per group"
                                chartData={dashboardContext.participantRate}
                                chartColors={
                                    dashboardContext.participantRate.map((item) => {
                                        return stringToColor(item.label)
                                    })
                                }
                            />
                        </Grid>

                        <Grid item xs={12} md={6} lg={8}>
                            <GroupHome groupList={dashboardContext.groupList} />
                        </Grid>

                        {/* <Grid item xs={12} md={6} lg={8}>
                            <AppWebsiteVisits
                                title="Group Activity"
                                subheader=""
                                chartLabels={[
                                    '01/01/2003',
                                    '02/01/2003',
                                    '03/01/2003',
                                    '04/01/2003',
                                    '05/01/2003',
                                    '06/01/2003',
                                    '07/01/2003',
                                    '08/01/2003',
                                    '09/01/2003',
                                    '10/01/2003',
                                    '11/01/2003',
                                ]}
                                chartData={[
                                    {
                                        name: 'Group A',
                                        type: 'column',
                                        fill: 'solid',
                                        data: [23, 11, 22, 27, 13, 22, 37, 21, 44, 22, 30],
                                    },
                                    {
                                        name: 'Group B',
                                        type: 'area',
                                        fill: 'gradient',
                                        data: [44, 55, 41, 67, 22, 43, 21, 41, 56, 27, 43],
                                    },
                                    {
                                        name: 'Group C',
                                        type: 'line',
                                        fill: 'solid',
                                        data: [30, 25, 36, 30, 45, 35, 64, 52, 59, 36, 39],
                                    },
                                ]}
                            />
                        </Grid> */}

                        {/* <Grid item xs={12} md={6} lg={4}>
                            <AppCurrentVisits
                                title="Actively Participated Group"
                                chartData={[
                                    { label: 'Group A', value: 20 },
                                ]}
                                chartColors={[
                                    theme.palette.chart.blue[0],
                                ]}
                            />
                        </Grid> */}

                        {/* <Grid item xs={12} md={6} lg={4}>
                            <AppEventTimeline
                                title="Upcoming Events"
                                list={[...Array(5)].map((_, index) => ({
                                    id: index,
                                    title: [
                                        'Event 1',
                                        'Event 2',
                                        'Event 3',
                                        'Event 4',
                                        'Event 5',
                                    ][index],
                                    type: `order${index + 1}`,
                                    time: '2022-05-14',
                                }))}
                            />
                        </Grid> */}

                    </Grid>
                </Container>
                <Snackbar
                    anchorOrigin={{ horizontal: 'center', vertical: 'bottom' }}
                    key={`bottom, center`}
                    open={dashboardContext.snackBarDetails.open}
                    autoHideDuration={6000}
                    onClose={handleSnackBarClose}>
                    <Alert onClose={handleSnackBarClose} severity={dashboardContext.snackBarDetails.type}>
                        {dashboardContext.snackBarDetails.message}
                    </Alert>
                </Snackbar>
            </DashboardProvider>

        </div>
    )
}

export default Dashboard;