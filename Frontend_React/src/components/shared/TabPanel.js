import React, { useReducer } from 'react';
import { createTheme, ThemeProvider } from '@mui/material/styles';

import TabPanelContainer from "./TabPanelContainer";
import palette from '../../styles/palette';
import { tabReducer, initialState } from '../../reducers/tabReducer'
import { TabProvider } from '../../providers/tabProvider'

const muiBaseTheme = createTheme();

const TabPanel = () => {
    const [tabContext, tabDispatch] = useReducer(tabReducer, initialState);
    return (
        <ThemeProvider
            theme={createTheme({
                typography: {
                    useNextVariants: true
                },
                palette,
                overrides: TabPanelContainer.getTheme(muiBaseTheme)
            })}
        >
            <TabProvider value={{ tabContext, tabDispatch }}>
                <TabPanelContainer />
            </TabProvider>
        </ThemeProvider>
    )
};
export { TabPanel as default }