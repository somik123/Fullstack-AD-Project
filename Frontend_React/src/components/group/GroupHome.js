import React, { useEffect, useReducer, useContext } from 'react';
// material
import { Typography, Stack, Container, Button, Icon } from '@mui/material';

import { useNavigate } from 'react-router-dom';
import GroupList from './GroupList';
import GroupSearch from './GroupSearch';
import Loading from './../shared/Loading';
// ----------------------------------------------------------------------
import { GroupProvider } from '../../providers/groupProvider'
import { TabContext } from '../../providers/tabProvider'
import { groupReducer, initialState } from '../../reducers/groupReducer'

const bookUrl = "https://java.team1ad.site/images/textbooks"

export default function GroupHome({ groupList }) {
    const { tabDispatch } = useContext(TabContext);
    const [groupContext, dispatch] = useReducer(groupReducer, initialState);
    let navigate = useNavigate();

    const createGroup = () => {
        tabDispatch({ type: 'SET_INDEX', index: 1 });
        navigate(`event`)
    }

    useEffect(() => {
        const initGroups = async () => {
            dispatch({ type: 'SET_LOADING', loading: true });
            groupList = groupList.filter(x=>!x.isPrivate);
            dispatch({
                type: 'SET_GROUP_LIST',
                groupList: groupList
            });
            dispatch({
                type: 'SET_FILTER_GROUP',
                filterGroupList: groupList
            });
            dispatch({ type: 'SET_LOADING', loading: false });
        };
        initGroups();
    }, []);

    if (groupContext.loading) return <div style={{ marginBottom: '20px' }}><Container> <Loading /></Container></div>;

    return (
        <div style={{ marginBottom: '20px' }}>
            <GroupProvider value={{ groupContext, dispatch }}>
                <Container>
                    <Stack direction="row" alignItems="center" justifyContent="space-between" mt={2}>
                        <Typography variant="h4" gutterBottom>
                            Study Group
                        </Typography>
                        <Button variant="contained" onClick={() => createGroup()} startIcon={<Icon>add</Icon>}>
                            New Group
                        </Button>
                    </Stack>

                    <Stack mb={2} direction="row" alignItems="center" justifyContent="space-between">
                        <GroupSearch />
                    </Stack>

                    <GroupList groups={groupContext.filterGroupList} tabDispatch={tabDispatch} />

                </Container>
            </GroupProvider>
        </div>
    );
}